package com.simats.Healthyfoodhabitapp.network

import android.content.Context
import android.util.Log
import com.simats.Healthyfoodhabitapp.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun getInstance(context: Context): Retrofit {
        val sessionManager = SessionManager(context)
        val baseUrl = sessionManager.getBaseUrl()
        Log.d("API_BASE_URL", baseUrl) // Logging the Base URL

        if (retrofit == null || retrofit?.baseUrl()?.toString() != baseUrl) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
