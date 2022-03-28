package com.example.imagesearchapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.imagesearchapp.api.UnSplashApi
import com.example.imagesearchapp.paging.UnSplashPagingSource
import javax.inject.Inject

class UnSplashRepository @Inject constructor(
    private val unSplashApi: UnSplashApi
) {
    fun getSearchResults(query:String) =
         Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {UnSplashPagingSource(unSplashApi, query) }
        ).liveData
    }
