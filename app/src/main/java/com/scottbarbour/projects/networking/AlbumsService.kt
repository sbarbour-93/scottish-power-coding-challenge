package com.scottbarbour.projects.networking

import com.scottbarbour.projects.model.Album
import retrofit2.http.GET

interface AlbumsService {
    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    @GET("/albums")
    suspend fun getListOfAlbums() : List<Album>
}