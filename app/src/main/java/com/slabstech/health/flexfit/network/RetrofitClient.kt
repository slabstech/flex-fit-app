// app/src/main/java/com/slabstech/health/flexfit/data/remote/RetrofitClient.kt
package com.slabstech.health.flexfit.data.remote

import android.content.Context
import com.slabstech.health.flexfit.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https:///"

    // We'll build the client lazily when context is provided
    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    // This is the ONLY function you call from the app
    fun getApiService(context: Context): ApiService {
        if (retrofit == null || okHttpClient == null) {
            okHttpClient = createOkHttpClient(context.applicationContext)
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient!!)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiService::class.java)
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val token = TokenManager.getToken(context)  // Now context is available!

                val requestBuilder = original.newBuilder()
                    .header("User-Agent", "FlexFit-Android/1.0")
                    .header("Accept", "application/json")
                    .header("Accept-Encoding", "gzip")
                    .header("Connection", "close")

                // Add Bearer token only if logged in (skip on login/register)
                if (!token.isNullOrBlank()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }

                // Critical for Cloudflare + FastAPI OAuth2 login
                if (original.method == "POST" && original.body?.contentType()?.toString()?.contains("form-urlencoded") == true) {
                    requestBuilder.header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }
}