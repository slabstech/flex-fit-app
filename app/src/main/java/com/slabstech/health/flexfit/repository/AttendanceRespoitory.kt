// repository/AttendanceRepository.kt
package com.slabstech.health.flexfit.repository

import com.slabstech.health.flexfit.data.remote.RetrofitClient
import com.slabstech.health.flexfit.data.remote.dto.AttendanceRequest
import com.slabstech.health.flexfit.data.remote.dto.AttendanceResponse
import retrofit2.Response

class AttendanceRepository(private val context: android.content.Context) {
    private val api = RetrofitClient.getApiService(context)

    suspend fun markAttendance(studentId: String, qrCode: String): Response<AttendanceResponse> {
        return api.markAttendance(AttendanceRequest(studentId, qrCode))
    }
}