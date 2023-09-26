package com.fjr619.newsloc.presentation.detail

import com.fjr619.newsloc.domain.model.Article

sealed class DetailEvent {

    data class GetBookmarkArticle(val article: Article?): DetailEvent()
    data class UpsertDeleteArticle(val article: Article) : DetailEvent()

//    object RemoveSideEffect : DetailEvent()

}