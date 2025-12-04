// app/src/main/java/com/slabstech/health/flexfit/data/remote/RetrofitClient.kt
package com.slabstech.health.flexfit.data.remote

import android.content.Context
import android.util.Log
import com.slabstech.health.flexfit.utils.TokenManager
import com.slabstech.health.flexfit.data.remote.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https:"

    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    fun getApiService(context: Context): ApiService {
        if (retrofit == null || okHttpClient == null) {

            // Best-practice logging interceptor — shows you clean JSON automatically
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                Log.d("API_OKHTTP", message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY   // Shows full request + response
            }

            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor) // ← This replaces manual peekBody completely
                .addInterceptor { chain ->
                    val original = chain.request()
                    val token = TokenManager.getToken(context)

                    val requestBuilder = original.newBuilder()
                        .header("User-Agent", "FlexFit-Android/1.0")
                        .header("Accept", "application/json")
                        // DO NOT touch Accept-Encoding → OkHttp handles gzip perfectly by default
                        .header("Connection", "close")

                    if (!token.isNullOrBlank()) {
                        requestBuilder.header("Authorization", "Bearer $token")
                    }

                    if (original.method == "POST" &&
                        original.body?.contentType()?.toString()?.contains("form-urlencoded") == true
                    ) {
                        requestBuilder.header(
                            "Content-Type",
                            "application/x-www-form-urlencoded; charset=utf-8"
                        )
                    }

                    chain.proceed(requestBuilder.build())
                }
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient!!)
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .build()
        }

        return retrofit!!.create(ApiService::class.java)
    }

    fun clear() {
        retrofit = null
        okHttpClient = null
    }
}