/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:34 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 10:47 PM
 *
 */
package pe.com.onecode.apoloapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import pe.com.onecode.apoloapp.model.entities.ResponseTracks
import pe.com.onecode.apoloapp.model.entities.Track
import pe.com.onecode.apoloapp.model.repository.SpotifyRepositoryImp

class TrackViewModel : ViewModel() {

    private val spotifyRepo by lazy { SpotifyRepositoryImp() }
    private val tracks: MutableLiveData<ArrayList<Track>> = MutableLiveData()
    private val currentTrack: MutableLiveData<String> = MutableLiveData()

    fun setTracksLiveData(artistId: String, token: String) {
        spotifyRepo.getTopTracks(artistId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ResponseTracks>() {
                    override fun onComplete() = Unit
                    override fun onNext(response: ResponseTracks) = subscribeTracks(response)
                    override fun onError(e: Throwable) = Unit
                })
    }

    fun setCurrentTrackLiveData(trackName: String) {
        currentTrack.value = trackName
    }

    fun getTracksLiveData() = tracks
    fun getCurrentTrackLiveData() = currentTrack

    private fun subscribeTracks(response: ResponseTracks) = tracks.postValue(response.tracks.take(7) as ArrayList<Track>)

}