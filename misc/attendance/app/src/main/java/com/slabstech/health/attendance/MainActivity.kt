package com.slabstech.health.attendance

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// ────────────────── DATA CLASSES & API ──────────────────
data class AttendanceRequest(
    val student_id: String,
    val qr_code: String
)

data class AttendanceResponse(
    val message: String,
    val student_name: String? = null,
    val timestamp: String? = null
)

interface ApiService {
    @POST("/attendance2")
    fun markAttendance(@Body request: AttendanceRequest): Call<AttendanceResponse>
}
// ───────────────────────────────────────────────────────

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    // CHANGE THESE TWO LINES ONLY
    private val STUDENT_ID = "STD001"                    // Your student ID
    private val SERVER_URL = "http://192.168.1.100:8000/" // Your PC IP (same Wi-Fi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tvStudentInfo).text = "Student ID: $STUDENT_ID"

        // ──────── Retrofit Setup ────────
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        // ─────────────────────────────────

        findViewById<Button>(R.id.btnScan).setOnClickListener {
            startQRScanner()
        }
    }

    private fun startQRScanner() {
        IntentIntegrator(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt("Scan today's attendance QR code")
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(false)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                markAttendance(result.contents.trim())
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun markAttendance(scannedQrCode: String) {
        val request = AttendanceRequest(
            student_id = STUDENT_ID,
            qr_code = scannedQrCode
        )

        apiService.markAttendance(request).enqueue(object : Callback<AttendanceResponse> {
            override fun onResponse(call: Call<AttendanceResponse>, response: Response<AttendanceResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Toast.makeText(
                        this@MainActivity,
                        "${body?.message}\nWelcome ${body?.student_name.orEmpty()}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val msg = when (response.code()) {
                        400 -> "Invalid QR or already attended today"
                        404 -> "Student not found"
                        else -> "Server error ${response.code()}"
                    }
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}