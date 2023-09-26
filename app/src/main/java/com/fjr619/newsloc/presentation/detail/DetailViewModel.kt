package com.fjr619.newsloc.presentation.detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.util.UIComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @AssistedInject constructor(
    private val newsUseCases: NewsUseCases,
    @Assisted private val article: Article?
) : ViewModel() {

    var sideEffect: MutableSharedFlow<UIComponent> = MutableSharedFlow()

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
                    Log.e("TAG", "launch GetBookmarkArticle")
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
        sideEffect.emit(UIComponent.Toast("Article deleted"))
    }

    private suspend fun upsertArticle(article: Article) {
        newsUseCases.upsertArticle(article = article)
        bookmarkArticle.value = article
        sideEffect.emit(UIComponent.Toast("Article Inserted"))
    }
}