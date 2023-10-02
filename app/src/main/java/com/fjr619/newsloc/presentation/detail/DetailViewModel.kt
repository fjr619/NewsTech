package com.fjr619.newsloc.presentation.detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.util.UiEffect
import com.fjr619.newsloc.util.UiText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    private val newsUseCases: NewsUseCases,
    @Assisted private val article: Article?
) : ViewModel() {

    private var _sideEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val sideEffect = _sideEffect.asSharedFlow()

    var bookmarkArticle = mutableStateOf<Article?>(null)
        private set

    @AssistedFactory
    interface Factory {
        fun create(article: Article?): DetailViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            article: Article?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(article) as T
            }
        }
    }


    init {
        Log.e("TAG", "init detail viewmodel $article")
        onEvent(DetailEvent.GetBookmarkArticle(article))
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
//            is DetailEvent.RemoveSideEffect ->{
//                sideEffect = null
//            }
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