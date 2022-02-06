package com.scottbarbour.projects.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scottbarbour.projects.model.Album
import com.scottbarbour.projects.networking.NetworkResult
import com.scottbarbour.projects.repositories.albums.AlbumsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumListViewModel(private val repository: AlbumsRepository,
                         private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {

    private val _albumList: MutableLiveData<List<Album>> = MutableLiveData(emptyList())
    val albumList: LiveData<List<Album>> = _albumList

    private val _isDownloading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDownloading: LiveData<Boolean> = _isDownloading

    private val _errorOccurred: MutableLiveData<Boolean> = MutableLiveData(false)
    val errorOccurred: LiveData<Boolean> = _errorOccurred


    init {
        refreshAlbumList()
    }

    private fun fetchListOfAlbums() {
        _isDownloading.postValue(true)
        _errorOccurred.postValue(false)
        viewModelScope.launch(coroutineDispatcher) {
            when (val response = repository.downloadListOfAlbums()) {
                is NetworkResult.Success -> {
                    _albumList.postValue(response.data)
                }
                is NetworkResult.Error -> {
                    _albumList.postValue(emptyList())
                    _errorOccurred.postValue(true)
                }
            }
            _isDownloading.postValue(false)
        }
    }

    fun refreshAlbumList() {
        fetchListOfAlbums()
    }
}