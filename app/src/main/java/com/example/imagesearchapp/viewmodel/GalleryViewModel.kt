package com.example.imagesearchapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.imagesearchapp.repository.UnSplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val unSplashRepository: UnSplashRepository
):ViewModel() {

    //paging
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    var photos = currentQuery.switchMap { queryString ->
        unSplashRepository.getSearchResults(queryString).cachedIn(viewModelScope)
    }


    fun searchPhotos(query:String){
        currentQuery.value = query
    }
    companion object{
        private const val DEFAULT_QUERY = "computers"
    }
}