package com.fjr619.newsloc.presentation.search

import androidx.paging.PagingData
import com.fjr619.newsloc.domain.model.Article
import kotlinx.coroutines.flow.Flow

data class SearchState(
    val searchQuery: String = "",
    val articles: Flow<PagingData<Article>>? = null
)