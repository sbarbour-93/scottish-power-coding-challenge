package com.scottbarbour.projects.ui.albums

import android.util.Log
import com.scottbarbour.projects.model.Album
import com.scottbarbour.projects.networking.AlbumsService
import com.scottbarbour.projects.networking.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AlbumsRepository(private val apiService : AlbumsService) {

    suspend fun downloadListOfAlbums() : NetworkResult<List<Album>> {
       return withContext(Dispatchers.IO) {

           return@withContext try {
               val apiResponse = apiService.getListOfAlbums()
               Log.i(AlbumsRepository::class.simpleName, "Successfully retrieved list of albums from API")
               NetworkResult.Success(apiResponse)
           } catch (error : Exception) {
               Log.e(AlbumsRepository::class.simpleName,
                   "Error thrown when downloading list of albums:\n${error.message}")
               NetworkResult.Error(error)
           }
       }
    }

}
