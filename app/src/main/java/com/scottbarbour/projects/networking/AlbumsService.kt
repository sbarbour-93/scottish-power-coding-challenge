package com.scottbarbour.projects.networking

import com.scottbarbour.projects.model.Album
import retrofit2.Call
import retrofit2.http.GET

interface AlbumsService {
    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    @GET("/albums")
    fun  getListOfAlbums() : Call<List<Album>>
}