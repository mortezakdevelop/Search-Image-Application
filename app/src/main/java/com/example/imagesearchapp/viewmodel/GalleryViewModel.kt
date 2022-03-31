package com.example.imagesearchapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.imagesearchapp.repository.UnSplashRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: UnSplashRepository
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query:String){
        currentQuery.value = query
    }
    companion object{
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "computers"
    }
}