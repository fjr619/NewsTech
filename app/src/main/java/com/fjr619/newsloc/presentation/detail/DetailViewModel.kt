package com.fjr619.newsloc.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.util.UiEffect
import com.fjr619.newsloc.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
//    @Assisted private val article: Article?
) : ViewModel() {

    private var _sideEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val sideEffect = _sideEffect.asSharedFlow()

    private var _bookmarkArticle = MutableStateFlow<Article?>(null)
    val bookMarkArticle = _bookmarkArticle.asStateFlow()

    private var _article = MutableStateFlow<Article?>(null)
    val article = _article.asStateFlow()

//    @AssistedFactory
//    interface Factory {
//        fun create(article: Article?): DetailViewModel
//    }

//    @Suppress("UNCHECKED_CAST")
//    companion object {
//        fun provideFactory(
//            assistedFactory: Factory,
//            article: Article?
//        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return assistedFactory.create(article) as T
//            }
//        }
//    }


//    init {
//        Log.e("TAG", "init detail viewmodel $article")
//        onEvent(DetailEvent.GetBookmarkArticle(article))
//    }

    private suspend fun getBookmarkArticle(article: Article?) {
        _bookmarkArticle.value = article?.let {
            newsUseCases.getArticle(url = it.url)
        }
    }


    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.GetDetailArticle -> {
                viewModelScope.launch {
                    this@DetailViewModel._article.value = event.article
                    getBookmarkArticle(event.article)
                }
            }

            is DetailEvent.UpsertDeleteArticle -> {
                viewModelScope.launch {
                    getBookmarkArticle(event.article)
                    if (_bookmarkArticle.value == null) {
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
        _bookmarkArticle.value = null
        _sideEffect.emit(UiEffect.Toast(UiText.DynamicString("Article deleted")))
    }

    private suspend fun upsertArticle(article: Article) {
        newsUseCases.upsertArticle(article = article)
        _bookmarkArticle.value = article
        _sideEffect.emit(UiEffect.Toast(UiText.DynamicString("Article Inserted")))
    }
}