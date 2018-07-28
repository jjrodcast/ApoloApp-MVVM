/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:33 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 12:58 AM
 *
 */
package pe.com.onecode.apoloapp.view

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.transition.*
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.airbnb.lottie.LottieDrawable
import kotlinx.android.synthetic.main.activity_main.*
import pe.com.onecode.apoloapp.R
import pe.com.onecode.apoloapp.model.entities.Artist
import pe.com.onecode.apoloapp.model.entities.Token
import pe.com.onecode.apoloapp.utilities.Extras
import pe.com.onecode.apoloapp.utilities.State
import pe.com.onecode.apoloapp.utilities.hideKeyboard
import pe.com.onecode.apoloapp.view.adapter.ArtistAdapter
import pe.com.onecode.apoloapp.viewmodel.ArtistViewModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private val artistViewModel by lazy { ViewModelProviders.of(this).get(ArtistViewModel::class.java) }
    private val artistAdapter by lazy { ArtistAdapter(R.layout.item_recycler_artist, this::onArtistClick) }
    private val layoutManager by lazy { LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    //region Initialize
    private fun init() {
        toolbar()
        showText(false)
        hideRecycler()
        setRecycler()
        getTokenLiveData()
        getArtistLiveData()
        getStateLiveData()
    }
    //endregion

    //region Toolbar
    private fun toolbar() {
        setSupportActionBar(toolbar as Toolbar)
    }
    //endregion

    //region Show/Hide Recycler Artist
    private fun showRecycler() {
        recyclerArtist.visibility = View.VISIBLE
    }

    private fun hideRecycler() {
        recyclerArtist.visibility = View.GONE
    }
    //endregion

    //region Show/Hide Text Default
    private fun showText(changeText: Boolean) {
        text_search.visibility = View.VISIBLE
        if (changeText) text_search.text = getString(R.string.no_artist)
        else text_search.text = getString(R.string.search_artist_label)
    }

    private fun hideText() {
        text_search.visibility = View.GONE
    }
    //endregion

    //region Create RecyclerView Configuration
    private fun setRecycler() {
        recyclerArtist.itemAnimator = DefaultItemAnimator()
        recyclerArtist.layoutManager = layoutManager
        recyclerArtist.adapter = artistAdapter
    }
    //endregion

    //region Start/Stop Loading Animation
    private fun startAnimation() {
        loader.visibility = View.VISIBLE
        loader.setAnimation("loading.json")
        loader.playAnimation()
        loader.repeatCount = LottieDrawable.INFINITE
    }

    private fun stopAnimation() {
        if (loader.isAnimating) {
            loader.repeatCount = 0
            loader.visibility = View.GONE
        }
    }
    //endregion

    //region Click Artist Event
    private fun onArtistClick(artist: Artist, sharedImageView: ImageView) {

        val intent = Intent(this, ArtistDetailActivity::class.java)
        // region Extras
        if (artist.images.size > 0) intent.putExtra(Extras.ARTIST_ITEM, artist.images[0].url)
        else intent.putExtra(Extras.ARTIST_ITEM, "")

        intent.putExtra(Extras.IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(sharedImageView))
        intent.putExtra(Extras.ARTIST_ID, artist.id)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedImageView,
                ViewCompat.getTransitionName(sharedImageView))
        //endregion
        startActivity(intent, options.toBundle())
    }
    //endregion

    //region ViewModel Events
    private fun getStateLiveData() {
        artistViewModel.getStateLiveData().observe(this, Observer<State> {
            when (it!!) {
                State.LOADING -> {
                    hideKeyboard()
                    hideText()
                    hideRecycler()
                    startAnimation()
                }
                State.COMPLETE -> {
                    stopAnimation()
                    showRecycler()
                    recyclerArtist.scrollToPosition(0)
                }
                State.EMPTY_DATA -> {
                    hideRecycler()
                    showText(true)
                }

            }
        })
    }

    private fun getArtistLiveData() {
        artistViewModel.getArtistsLiveData().observe(this, Observer<ArrayList<Artist>> {
            artistAdapter.addData(it!!)
        })
    }

    private fun getTokenLiveData() {
        artistViewModel.getTokenLiveData().observe(this, Observer<Token> {
            if (it!!.token.isNotEmpty()) {
                observeArtistResponse(it.token)
                saveTokePreferences(it.token)
            }
        })
    }

    private fun observeTokenResponse() {
        artistViewModel.setTokenLiveData()
    }

    private fun observeArtistResponse(token: String) {
        artistViewModel.setArtistsLiveData(token)
    }
    //endregion

    //region Menu Creation and Option Selected
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        // associate the configuration with the search option
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val transition = AutoTransition()
                transition.duration = resources.getInteger(R.integer.transition_duration).toLong()
                TransitionManager.beginDelayedTransition(toolbar as ViewGroup, transition)
                item.expandActionView()
            }
        }
        return true
    }
    //endregion

    //region Search Toolbar
    override fun onQueryTextSubmit(query: String): Boolean {
        if (!query.isEmpty()) {
            artistViewModel.setQueryLiveDate(query)
            observeTokenResponse()
        }
        return false
    }

    override fun onQueryTextChange(newText: String) = false
    //endregion

    //region ShredPreferences
    private fun saveTokePreferences(token: String) {
        getSharedPreferences(Extras.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .apply { putString(Extras.TOKEN, token) }
                .apply()
    }
    //endregion
}
