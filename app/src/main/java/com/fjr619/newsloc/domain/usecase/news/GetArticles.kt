package com.fjr619.newsloc.domain.usecase.news

import com.fjr619.newsloc.data.local.NewsDao
import com.fjr619.newsloc.domain.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticles @Inject constructor(
    private val newsDao: NewsDao
) {

    operator fun invoke(): Flow<List<Article>> {
        return newsDao.getArticles()
    }

}