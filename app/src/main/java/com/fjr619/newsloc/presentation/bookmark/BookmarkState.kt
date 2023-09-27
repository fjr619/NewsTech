package com.fjr619.newsloc.presentation.bookmark

import com.fjr619.newsloc.domain.model.Article

data class BookmarkState(
    val articles: List<Article> = emptyList()
)