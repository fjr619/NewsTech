package com.fjr619.newsloc.presentation.detail

import com.fjr619.newsloc.domain.model.Article

sealed class DetailEvent {

    data class GetDetailArticle(val article: Article?): DetailEvent()
    data class UpsertDeleteArticle(val article: Article) : DetailEvent()

//    object RemoveSideEffect : DetailEvent()

}