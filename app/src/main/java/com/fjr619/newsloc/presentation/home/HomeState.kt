package com.fjr619.newsloc.presentation.home

import androidx.paging.PagingData
import com.fjr619.newsloc.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class HomeState(
    val newsList: Flow<PagingData<Article>> = flow { PagingData.empty<Article>() },
)