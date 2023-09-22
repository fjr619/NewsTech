package com.fjr619.newsloc.domain.usecase.news

import androidx.paging.PagingData
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNews @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(searchQuery: String, sources: List<String>): Flow<PagingData<Article>> {
        return newsRepository.searchNews(
            searchQuery = searchQuery,
            sources = sources
        )
    }
}