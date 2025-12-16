// ui/attendance/AttendanceFragment.kt
package com.slabstech.health.flexfit.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.slabstech.health.flexfit.databinding.FragmentAttendanceBinding
import com.slabstech.health.flexfit.repository.AttendanceRepository

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AttendanceViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AttendanceViewModel(AttendanceRepository(requireContext())) as T
            }
        }
    }

    private val studentId: String
        get() = "STD001" // ← Change or load from SharedPrefs / login later

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvStudentInfo.text = "Student ID: $studentId"
        binding.btnScan.setOnClickListener { startScanner() }

        viewModel.result.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                Toast.makeText(
                    requireContext(),
                    "${response.message}\nWelcome ${response.student_name.orEmpty()}",
                    Toast.LENGTH_LONG
                ).show()
            }.onFailure { exception ->
                Toast.makeText(requireContext(), exception.message ?: "Error", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnScan.isEnabled = !loading
        }
    }

    private fun startScanner() {
        IntentIntegrator.forSupportFragment(this)
            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            .setPrompt("Scan today's gym QR code")
            .setBeepEnabled(true)
            .initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result?.contents != null) {
            viewModel.markAttendance(studentId, result.contents.trim())
        } else if (result == null) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}