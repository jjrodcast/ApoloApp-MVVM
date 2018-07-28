/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.view.adapter

import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_recycler_artist.view.*
import pe.com.onecode.apoloapp.R
import pe.com.onecode.apoloapp.model.entities.Artist
import pe.com.onecode.apoloapp.utilities.GlideApp
import pe.com.onecode.apoloapp.utilities.inflate

class ArtistAdapter(@LayoutRes private val layout: Int,
                    private val artistListener: (artist: Artist, sharedImageView: ImageView) -> Unit = { _, _ -> }) :
        RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {

    private val artists = arrayListOf<Artist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(layout))

    override fun getItemCount() = artists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artists[position], artistListener)
    }

    fun addData(artistList: ArrayList<Artist>) {
        artists.clear()
        artists.addAll(artistList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(artist: Artist, artistListener: (artist: Artist, sharedImageView: ImageView) -> Unit) = with(itemView) {

            if (artist.images.size > 0) {
                GlideApp.with(context)
                        .load(artist.images[0].url)
                        .centerInside()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .into(artistImage)
            } else {
                GlideApp.with(context)
                        .load(R.drawable.placeholder)
                        .centerInside()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(artistImage)
            }

            ViewCompat.setTransitionName(artistImage, artist.name.plus(adapterPosition))
            artistImage.setOnClickListener { artistListener(artist, artistImage) }
            artistName.isSelected = true
            artistName.text = artist.name
            artistFollowers.text = artist.follower.total.toString()
            artistPopularity.text = artist.popularity.toString()
        }
    }
}