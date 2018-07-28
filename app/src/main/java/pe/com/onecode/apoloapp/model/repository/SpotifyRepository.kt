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

interface SpotifyRepository {

    fun getToken(): Observable<Token>

    fun getArtists(query: String, token: String, type: String, market: String, limit: Int): Observable<ResponseData>

    fun getTopTracks(artistId: String, token: String): Observable<ResponseTracks>
}