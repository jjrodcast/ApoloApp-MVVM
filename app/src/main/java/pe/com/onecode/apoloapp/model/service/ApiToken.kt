/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.model.service

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiToken {

    // Create a Spotify Developer Account and change CLIENT_ID and CLIENTE_SECRET
    companion object {
        const val BASE_URL_TOKEN = "https://accounts.spotify.com/api/"
        const val GRANT_TYPE = "client_credentials"
        const val CLIENT_ID = "put_your_client_id" // Put your own
        const val CLIENT_SECRET = "put_your_secret" // Put your own

        val retrofit: ApiTokenService by lazy {
            Retrofit.Builder()
                    .baseUrl(BASE_URL_TOKEN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiTokenService::class.java)
        }
    }
}