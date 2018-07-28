/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.model.service

import io.reactivex.Observable
import pe.com.onecode.apoloapp.model.entities.ResponseData
import pe.com.onecode.apoloapp.model.entities.ResponseTracks
import retrofit2.http.*

interface ApiService {

    //	https://api.spotify.com/v1/search
    @Headers(value = ["Accept: application/json", "Content-Type: application/json"])
    @GET(value = "search")
    fun getArtists(@Query("q") query: String,
                   @Query("type") type: String,
                   @Query("market") market: String,
                   @Query("limit") limit: Int): Observable<ResponseData>

    //https://api.spotify.com/v1/artists/{id}/top-tracks
    @Headers(value = ["Accept: application/json", "Content-Type: application/json"])
    @GET(value = "artists/{id}/top-tracks")
    fun getTopTracks(@Path("id") artistId: String, @Query("country") country: String) : Observable<ResponseTracks>
}