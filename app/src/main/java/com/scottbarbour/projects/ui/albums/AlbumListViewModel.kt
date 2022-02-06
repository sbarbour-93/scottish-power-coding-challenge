package com.scottbarbour.projects.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scottbarbour.projects.model.Album

class AlbumListViewModel(private val repository: AlbumsRepository) : ViewModel() {

    private val _albumList: MutableLiveData<List<Album>> = MutableLiveData(emptyList())
    val albumList: LiveData<List<Album>> = _albumList

    private val _isDownloading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDownloading: LiveData<Boolean> = _isDownloading

    init {
        fetchListOfAlbums()
    }

    private fun fetchListOfAlbums() {
        _isDownloading.postValue(true)
        val albumList = repository.getListOfAlbums()
        _isDownloading.postValue(false)
        _albumList.postValue(albumList)
    }

    fun refreshAlbumList() {
        fetchListOfAlbums()
    }
}