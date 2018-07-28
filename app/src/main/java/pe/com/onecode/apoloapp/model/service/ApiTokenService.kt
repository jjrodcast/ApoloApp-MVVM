/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.model.service

import io.reactivex.Observable
import pe.com.onecode.apoloapp.model.entities.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiTokenService {

    @FormUrlEncoded
    @POST(value = "token")
    fun generateToken(@Field("grant_type") grantType: String, @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String): Observable<Token>

}