/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:33 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:33 PM
 *
 */
package pe.com.onecode.apoloapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pe.com.onecode.apoloapp.model.entities.Artist
import pe.com.onecode.apoloapp.model.entities.ResponseData
import pe.com.onecode.apoloapp.model.entities.Token
import pe.com.onecode.apoloapp.model.repository.SpotifyRepositoryImp
import pe.com.onecode.apoloapp.utilities.State

class ArtistViewModel : ViewModel() {

    private val spotifyRepo by lazy { SpotifyRepositoryImp() }
    private val artists: MutableLiveData<ArrayList<Artist>> = MutableLiveData()
    private val token: MutableLiveData<Token> = MutableLiveData()
    private val state: MutableLiveData<State> = MutableLiveData()
    private val mQuery: MutableLiveData<String> = MutableLiveData()

    fun getTokenLiveData() = token
    fun getArtistsLiveData() = artists
    fun getStateLiveData() = state
    //fun getQuery() = mQuery.value // Not necessary for this demo, if required use it !!!

    fun setTokenLiveData() {
        state.value = State.LOADING
        spotifyRepo.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeToken)
    }

    fun setQueryLiveDate(query: String) {
        mQuery.value = query
    }

    fun setArtistsLiveData(token: String) {
        spotifyRepo.getArtists(mQuery.value!!, token, "artist", "ES", 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeArtist)
    }

    private fun subscribeToken(response: Token) = token.postValue(response)

    private fun subscribeArtist(response: ResponseData) {
        state.value = if (response.data.artists.size > 0) State.COMPLETE else State.EMPTY_DATA
        artists.postValue(response.data.artists)
    }

}