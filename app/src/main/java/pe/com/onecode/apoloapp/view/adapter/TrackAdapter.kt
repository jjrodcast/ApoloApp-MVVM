/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.view.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.item_recycler_track.view.*
import pe.com.onecode.apoloapp.R
import pe.com.onecode.apoloapp.model.entities.Track
import pe.com.onecode.apoloapp.utilities.GlideApp
import pe.com.onecode.apoloapp.utilities.inflate
import java.util.ArrayList

class TrackAdapter(@LayoutRes private val layout: Int,
                   private val listener: (track: Track, imageView: ImageView) -> Unit = { _, _ -> }) :
        RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    private val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(layout))

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }

    fun addTracks(trackList: ArrayList<Track>) {
        tracks.clear()
        tracks.addAll(trackList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(track: Track, trackListener: (track: Track, imageView: ImageView) -> Unit) = with(itemView) {

            if (track.album.images.size > 0) {
                GlideApp.with(context)
                        .load(track.album.images[0].url)
                        .transforms(CenterInside(), RoundedCorners(resources.getInteger(R.integer.offset)))
                        .placeholder(R.drawable.placeholder)
                        .into(trackImage)
            } else {
                GlideApp.with(context)
                        .load(R.drawable.placeholder)
                        .transforms(CenterInside(), RoundedCorners(resources.getInteger(R.integer.offset)))
                        .into(trackImage)
            }

            trackName.isSelected = true
            trackName.text = track.name
            trackAlbum.isSelected = true
            trackAlbum.text = track.album.name
            trackImagePlayer.transitionName = "trackImagePlat".plus(adapterPosition)
            if (track.previewUrl.isNullOrEmpty()) {
                trackImagePlayer.setImageDrawable(context.getDrawable(R.drawable.ic_play_disable))
                trackImagePlayer.isEnabled = false
            }

            trackImagePlayer.setOnClickListener { trackListener(track, it as @kotlin.ParameterName(name = "imageView") ImageView) }
        }
    }

}