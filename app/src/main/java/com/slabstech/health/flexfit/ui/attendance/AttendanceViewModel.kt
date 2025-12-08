// ui/attendance/AttendanceViewModel.kt
package com.slabstech.health.flexfit.ui.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.AttendanceResponse
import com.slabstech.health.flexfit.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {

    private val _result = MutableLiveData<Result<AttendanceResponse>>()
    val result: LiveData<Result<AttendanceResponse>> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun markAttendance(studentId: String, qrCode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.markAttendance(studentId, qrCode)
                if (response.isSuccessful && response.body() != null) {
                    _result.value = Result.success(response.body()!!)
                } else {
                    val msg = when (response.code()) {
                        400 -> "Invalid QR code or already marked today"
                        401 -> "Please login again"
                        404 -> "Student not found"
                        else -> "Server error (${response.code()})"
                    }
                    _result.value = Result.failure(Exception(msg))
                }
            } catch (e: Exception) {
                _result.value = Result.failure(Exception("Network error: ${e.message}"))
            } finally {
                _isLoading.value = false
            }
        }
    }
}