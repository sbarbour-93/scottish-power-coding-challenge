package com.scottbarbour.projects.ui.albums

import com.scottbarbour.projects.model.Album

class AlbumsRepository {

    private val albumList = listOf(
        Album(1, 1, "Californication"),
        Album(2, 1, "Red"),
        Album(3, 1, "No More Idols")
    )
    fun getListOfAlbums() : List<Album> {
        return albumList
    }

}
