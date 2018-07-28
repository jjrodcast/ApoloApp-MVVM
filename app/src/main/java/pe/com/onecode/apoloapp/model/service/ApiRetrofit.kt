/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.model.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiRetrofit {

    companion object {
        private const val BASE_URL = "https://api.spotify.com/v1/"

        fun retrofit(token: String): ApiService {
            return Retrofit.Builder()
                    .client(setHeaders(token))
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiService::class.java)!!
        }

        private fun setHeaders(token: String): OkHttpClient {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor {
                        val request = it.request()
                        val newRequest = request.newBuilder().header("Authorization", "Bearer $token")
                        it.proceed(newRequest.build())
                    }
            return okHttpClient.build()
        }
    }
}