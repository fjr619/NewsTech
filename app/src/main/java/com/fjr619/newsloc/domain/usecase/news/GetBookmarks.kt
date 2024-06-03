package com.fjr619.newsloc.domain.usecase.news

import androidx.paging.PagingData
import com.fjr619.newsloc.data.local.NewsDao
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarks @Inject constructor(
    private val newsRepository: NewsRepository
) {

    operator fun invoke(): Flow<PagingData<Article>> {
        return newsRepository.getBookmarks()
    }

}