/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_artist_detail.*
import pe.com.onecode.apoloapp.R
import pe.com.onecode.apoloapp.model.entities.Track
import pe.com.onecode.apoloapp.utilities.Extras
import pe.com.onecode.apoloapp.utilities.GlideApp
import pe.com.onecode.apoloapp.utilities.toast
import pe.com.onecode.apoloapp.view.adapter.TrackAdapter
import pe.com.onecode.apoloapp.view.decorator.MarginDecorator
import pe.com.onecode.apoloapp.viewmodel.TrackViewModel

class ArtistDetailActivity : AppCompatActivity() {

    private val trackViewModel by lazy { ViewModelProviders.of(this).get(TrackViewModel::class.java) }
    private val trackAdapter by lazy { TrackAdapter(R.layout.item_recycler_track, this::onTrackClick) }
    private val layoutManager by lazy { LinearLayoutManager(this) }
    private val marginDecorator by lazy { MarginDecorator(this, R.integer.offset) }
    private var player: MediaPlayer? = null
    private var currentView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)
        init()
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    //region Initialize
    private fun init() {
        val extras = intent.extras
        loadImageDetail(extras)
        setRecycler()
        setTracksLiveData(extras.getString(Extras.ARTIST_ID, ""), getToken())
        getTracksLiveData()
        getCurrentTrackLiveData()
    }
    //endregion

    //region Load Image with Shared Animation
    private fun loadImageDetail(extras: Bundle) {

        supportStartPostponedEnterTransition()

        val url = if (extras.getString(Extras.ARTIST_ITEM) == "") R.drawable.placeholder else extras.getString(Extras.ARTIST_ITEM)
        val imageTransition = extras.getString(Extras.IMAGE_TRANSITION_NAME)

        artistImageDetail.transitionName = imageTransition

        GlideApp.with(this)
                .load(url)
                .transforms(CenterInside(), RoundedCorners(resources.getInteger(R.integer.offset)))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }
                })
                .into(artistImageDetail)
    }
    //endregion

    //region Set RecyclerView
    private fun setRecycler() {
        recyclerTrack.layoutManager = layoutManager
        recyclerTrack.addItemDecoration(marginDecorator)
        recyclerTrack.adapter = trackAdapter
    }
    //endregion

    //region ViewModel LiveDate
    private fun getTracksLiveData() {
        trackViewModel.getTracksLiveData().observe(this, Observer<ArrayList<Track>> {
            trackAdapter.addTracks(it!!)
        })
    }

    private fun setTracksLiveData(artistId: String, token: String) {
        trackViewModel.setTracksLiveData(artistId, token)
    }

    private fun getCurrentTrackLiveData() {
        trackViewModel.getCurrentTrackLiveData().observe(this, Observer<String> { trackPlaying.text = it!! })
    }

    private fun setCurrentTrackLiveData(trackUrl: String) {
        trackViewModel.setCurrentTrackLiveData(trackUrl)
    }
    //endregion

    //region Click Track to Play Music
    private fun onTrackClick(track: Track, imageView: ImageView) {

        if (track.previewUrl.isEmpty()) {
            toast("${track.name} ${getString(R.string.track_unavailable)}")
            return
        }

        //val view: View? = trackAdapter.getTrackView(position)
        var currentTrack = track.name

        if (currentView != null) {
            player?.let {
                if (player!!.isPlaying) {
                    if (imageView == currentView) {
                        stop(imageView)
                        currentTrack = ""
                    } else {
                        stop(currentView)
                        initializePlayer(imageView, track.previewUrl)
                    }
                }
            } ?: kotlin.run {
                initializePlayer(imageView, track.previewUrl)
            }
        } else {
            initializePlayer(imageView, track.previewUrl)
        }

        setCurrentTrackLiveData(currentTrack)
    }
    //endregion

    //region SharedPreferences Token
    private fun getToken(): String {
        return getSharedPreferences(Extras.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(Extras.TOKEN, "")
    }
    //endregion

    //region MediaPlayer
    private fun createPlayer(trackUrl: String = "") {
        if (player == null) {
            player = MediaPlayer.create(this, Uri.parse(trackUrl))
            player!!.isLooping = false
        }
    }

    private fun initializePlayer(view: ImageView, trackUrl: String) {
        currentView = view
        createPlayer(trackUrl)
        player!!.setOnCompletionListener { stop(view) }
        start(view)
    }

    private fun start(view: ImageView? = null) {
        view?.setImageDrawable(getDrawable(R.drawable.ic_pause_bar))
        player?.start()
    }

    private fun stop(view: ImageView? = null) {
        view?.setImageDrawable(getDrawable(R.drawable.ic_play_arrow))
        setCurrentTrackLiveData("")
        player?.stop()
        player?.release()
        player = null
    }
//endregion
}
