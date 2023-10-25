package com.fjr619.newsloc.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.util.composestateevents.consumed
import com.fjr619.newsloc.util.composestateevents.triggered
import com.fjr619.newsloc.util.snackbar.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val newsUseCases: NewsUseCases,
//    @Assisted private val article: Article?
) : ViewModel() {

//    private var _sideEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
//    val sideEffect = _sideEffect.asSharedFlow()
//
//    private var _bookmarkArticle = MutableStateFlow<Article?>(null)
//    val bookMarkArticle = _bookmarkArticle.asStateFlow()
//
//    private var _article = MutableStateFlow<Article?>(null)
//    val article = _article.asStateFlow()

  private var _viewState = MutableStateFlow<DetailViewState>(DetailViewState())
  val viewState = _viewState.asStateFlow()

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
    _viewState.update {
      it.copy(
        bookmark = article?.let {
          newsUseCases.getArticle(url = it.url)
        }
      )
    }
  }


  fun onEvent(event: DetailEvent) {
    when (event) {
      is DetailEvent.GetDetailArticle -> {
        viewModelScope.launch {
          _viewState.update {
            it.copy(
              article = event.article
            )
          }
          getBookmarkArticle(event.article)
        }
      }

      is DetailEvent.UpsertDeleteArticle -> {
        viewModelScope.launch {
          getBookmarkArticle(event.article)
          if (viewState.value.bookmark == null) {
            upsertArticle(article = event.article)
          } else {
            deleteArticle(article = event.article)
          }
        }
      }
    }
  }

  fun onConsumedSucceededEvent() {
    _viewState.update {
      it.copy(
        processSucceededEvent = consumed()
      )
    }
  }

  private suspend fun deleteArticle(article: Article) {
    newsUseCases.deleteArticle(article = article)
    _viewState.update {
      it.copy(
        bookmark = null,
        processSucceededEvent = triggered(UserMessage.Text("Article deleted"))
      )
    }
  }

  private suspend fun upsertArticle(article: Article) {
    newsUseCases.upsertArticle(article = article)
    _viewState.update {
      it.copy(
        bookmark = article,
        processSucceededEvent = triggered(UserMessage.Text("Article inserted"))
      )
    }
  }
}