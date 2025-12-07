package com.slabstech.health.flexfit.ui.attendance
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.slabstech.health.flexfit.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class AttendanceRequest(val student_id: String, val qr_code: String)
data class AttendanceResponse(val message: String, val student_name: String? = null, val timestamp: String? = null)

interface ApiService {
    @POST("/attendance2")
    fun markAttendance(@Body request: AttendanceRequest): Call<AttendanceResponse>
}

class AttendanceFragment : Fragment() {

    private lateinit var apiService: ApiService
    private val STUDENT_ID = "STD001" // Change here
    private val SERVER_URL = "https://stats.flex-fitness.club/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_attendance, container, false)

        // Setup Retrofit
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        view.findViewById<TextView>(R.id.tvStudentInfo).text = "Student ID: $STUDENT_ID"
        view.findViewById<Button>(R.id.btnScan).setOnClickListener { startQRScanner() }

        return view
    }

    private fun startQRScanner() {
        IntentIntegrator.forSupportFragment(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt("Scan today's attendance QR code")
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(false)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Scan cancelled", Toast.LENGTH_SHORT).show()
            } else {
                markAttendance(result.contents.trim())
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun markAttendance(scannedQrCode: String) {
        val request = AttendanceRequest(STUDENT_ID, scannedQrCode)

        apiService.markAttendance(request).enqueue(object : Callback<AttendanceResponse> {
            override fun onResponse(call: Call<AttendanceResponse>, response: Response<AttendanceResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Toast.makeText(
                        requireContext(),
                        "${body?.message}\nWelcome ${body?.student_name.orEmpty()}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val msg = when (response.code()) {
                        400 -> "Invalid QR or already attended"
                        404 -> "Student not found"
                        else -> "Server error: ${response.code()}"
                    }
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}