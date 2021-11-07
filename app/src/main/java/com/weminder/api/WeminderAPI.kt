package com.weminder.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeminderAPI {

    companion object {

        private fun <T> builder(service: Class<T>): T {
            val client = OkHttpClient.Builder().build()
            return Retrofit.Builder()
                .baseUrl("http://10.0.0.1:5000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(service)
        }

        fun api(): APIResources {
            return builder(APIResources::class.java)
        }

    }

}