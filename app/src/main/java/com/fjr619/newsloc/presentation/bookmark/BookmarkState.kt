package com.fjr619.newsloc.presentation.bookmark

import androidx.paging.PagingData
import com.fjr619.newsloc.domain.model.Article
import kotlinx.coroutines.flow.Flow

data class BookmarkState(
    val articles: Flow<PagingData<Article>>? = null
)