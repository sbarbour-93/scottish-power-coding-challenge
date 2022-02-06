package com.scottbarbour.projects.repositories.albums

import com.scottbarbour.projects.model.Album
import com.scottbarbour.projects.networking.AlbumsService
import com.scottbarbour.projects.networking.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AlbumsRepository(private val apiService : AlbumsService,
                       private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun downloadListOfAlbums() : NetworkResult<List<Album>> {
       return withContext(coroutineDispatcher) {

           return@withContext try {
               val apiResponse = apiService.getListOfAlbums()
               NetworkResult.Success(apiResponse)
           } catch (error : Exception) {
               NetworkResult.Error(error)
           }
       }
    }

}
