// File: app/src/main/java/com/slabstech/health/flexfit/data/remote/RetrofitClient.kt
package com.slabstech.health.flexfit.data.remote

import android.content.Context
import com.slabstech.health.flexfit.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://"

    private fun getOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()

                val token = TokenManager.getToken(context)

                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .header("User-Agent", "FlexFit-Android/1.0")   // THIS FIXES CLOUDFLARE 400!
                    .header("Accept", "application/json")
                    .method(original.method, original.body)

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    fun getApiService(context: Context): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(context))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}