package com.fjr619.newsloc.domain.usecase.news

data class NewsUseCases(
    val getNews: GetNews,
    val searchNews: SearchNews,
    val upsertArticle: UpsertArticle,
    val deleteArticle: DeleteArticle,
    val getBookmarks: GetBookmarks,
    val getArticle: GetArticle
)