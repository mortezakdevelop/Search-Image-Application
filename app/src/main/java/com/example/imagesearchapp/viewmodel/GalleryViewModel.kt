package com.example.imagesearchapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.imagesearchapp.repository.UnSplashRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val unSplashRepository: UnSplashRepository,
    @Assisted state:SavedStateHandle
):ViewModel() {

    //paging
    private val currentQuery = state.getLiveData(CURRENT_QUERY,DEFAULT_QUERY)

    var photos = currentQuery.switchMap { queryString ->
        unSplashRepository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query:String){
        currentQuery.value = query
    }
    companion object{
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "computers"
    }
}