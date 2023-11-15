package com.fjr619.newsloc.presentation.home

import androidx.paging.PagingData
import com.fjr619.newsloc.domain.model.Article
import kotlinx.coroutines.flow.Flow

data class HomeState(
    val newsTicker: String = "",
    val newsList: Flow<PagingData<Article>>? = null,
    val isLoading: Boolean = false,
)