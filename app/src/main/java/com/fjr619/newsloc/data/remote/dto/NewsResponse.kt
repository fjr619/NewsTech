package com.fjr619.newsloc.data.remote.dto

import com.fjr619.newsloc.domain.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)