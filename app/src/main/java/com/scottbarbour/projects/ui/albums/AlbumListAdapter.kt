package com.scottbarbour.projects.ui.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scottbarbour.projects.databinding.AlbumViewHolderBinding
import com.scottbarbour.projects.model.Album

class AlbumListAdapter(private val listOfAlbums : List<Album>) :
    RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val binding: AlbumViewHolderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumViewHolderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        with(holder) {
            with(listOfAlbums[position]) {
                binding.albumTitle.text = title
            }
        }
    }

    override fun getItemCount() = listOfAlbums.size
}