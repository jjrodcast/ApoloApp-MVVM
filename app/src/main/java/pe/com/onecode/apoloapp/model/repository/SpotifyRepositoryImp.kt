/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.model.repository

import io.reactivex.Observable
import pe.com.onecode.apoloapp.model.entities.*
import pe.com.onecode.apoloapp.model.service.ApiRetrofit
import pe.com.onecode.apoloapp.model.service.ApiToken

class SpotifyRepositoryImp : SpotifyRepository {

    override fun getToken(): Observable<Token> {
        return ApiToken.retrofit.generateToken(ApiToken.GRANT_TYPE, ApiToken.CLIENT_ID, ApiToken.CLIENT_SECRET)
    }

    override fun getArtists(query: String, token: String, type: String, market: String, limit: Int): Observable<ResponseData> {
        return ApiRetrofit.retrofit(token).getArtists(query, type, market, limit)
    }

    override fun getTopTracks(artistId: String, token: String): Observable<ResponseTracks> {
        return ApiRetrofit.retrofit(token).getTopTracks(artistId, "ES")
    }

}