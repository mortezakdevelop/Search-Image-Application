package com.example.imagesearchapp.paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagesearchapp.api.UnSplashApi
import com.example.imagesearchapp.model.UnSplashPhoto
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnSplashPagingSource(
    private val unsplashApi: UnSplashApi,
    private val query: String
) : PagingSource<Int, UnSplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnSplashPhoto> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnSplashPhoto>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.minus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)

        }

    }
}