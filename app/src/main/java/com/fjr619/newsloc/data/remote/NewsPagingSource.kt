package com.fjr619.newsloc.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fjr619.newsloc.data.remote.dto.ErrorResponse
import com.fjr619.newsloc.domain.model.Article
import com.google.gson.Gson
import retrofit2.HttpException

class NewsPagingSource(
    private val newsApi: NewsApi,
    private val sources: String
) : PagingSource<Int, Article>() {


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val newsResponse = newsApi.getNews(sources = sources, page = page)
            totalNewsCount += newsResponse.articles.size
            val articles = newsResponse.articles.distinctBy { it.title } //Remove duplicates

            LoadResult.Page(
                data = articles,
                nextKey = if (totalNewsCount == newsResponse.totalResults) null else page + 1,
                prevKey = null
            )
        } catch (e: HttpException) {
            e.printStackTrace()

            val errorResponse = e.response()?.run {
                errorBody()?.let {
                    val errorJson = it.string()
                    Gson().fromJson(errorJson, ErrorResponse::class.java)
                }
            }

            LoadResult.Error(
                throwable = Throwable(message = errorResponse?.message ?: "AAAA")
            )
        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }
    }
}