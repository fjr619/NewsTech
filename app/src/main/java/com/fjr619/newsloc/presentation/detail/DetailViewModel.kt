package com.fjr619.newsloc.presentation.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.util.UiEffect
import com.fjr619.newsloc.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _sideEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val sideEffect = _sideEffect.asSharedFlow()

    var bookmarkArticle = mutableStateOf<Article?>(null)
        private set

    init {
        onEvent(DetailEvent.GetBookmarkArticle(savedStateHandle.get<Article>("article")))
    }

    private suspend fun getBookmarkArticle(article: Article?) {
        bookmarkArticle.value = article?.let {
            newsUseCases.getArticle(url = it.url)
        }
    }


    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.GetBookmarkArticle -> {
                viewModelScope.launch {
                    getBookmarkArticle(event.article)
                }
            }

            is DetailEvent.UpsertDeleteArticle -> {
                viewModelScope.launch {
                    getBookmarkArticle(event.article)
                    if (bookmarkArticle.value == null) {
                        upsertArticle(article = event.article)
                    } else {
                        deleteArticle(article = event.article)
                    }
                }
            }
        }
    }

    private suspend fun deleteArticle(article: Article) {
        newsUseCases.deleteArticle(article = article)
        bookmarkArticle.value = null
        _sideEffect.emit(UiEffect.Toast(UiText.DynamicString("Article deleted")))
    }

    private suspend fun upsertArticle(article: Article) {
        newsUseCases.upsertArticle(article = article)
        bookmarkArticle.value = article
        _sideEffect.emit(UiEffect.Toast(UiText.DynamicString("Article Inserted")))
    }
}