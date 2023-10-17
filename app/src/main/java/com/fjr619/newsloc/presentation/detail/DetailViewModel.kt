package com.fjr619.newsloc.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
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

  private var _detailViewState = MutableStateFlow<DetailViewState>(DetailViewState())
  val detailViewState = _detailViewState.asStateFlow()

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
    _detailViewState.update {
      it.copy(
        bookmarkArticle = article?.let {
          newsUseCases.getArticle(it.url)
        }
      )
    }
  }


  fun onEvent(event: DetailEvent) {
    when (event) {
      is DetailEvent.GetDetailArticle -> {
        viewModelScope.launch {
          _detailViewState.update {
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
          if (detailViewState.value.bookmarkArticle == null) {
            upsertArticle(article = event.article)
          } else {
            deleteArticle(article = event.article)
          }
        }
      }
    }
  }

  fun onComsumedSuccessEvent() {
    _detailViewState.update {
      it.copy(
        processSucessEvent = consumed()
      )
    }
  }

  private suspend fun deleteArticle(article: Article) {
    newsUseCases.deleteArticle(article = article)
    _detailViewState.update {
      it.copy(
        bookmarkArticle = null,
        processSucessEvent = triggered(UiText.DynamicString("Article deleted"))
      )
    }
  }

  private suspend fun upsertArticle(article: Article) {
    newsUseCases.upsertArticle(article = article)
    _detailViewState.update {
      it.copy(
        bookmarkArticle = article,
        processSucessEvent = triggered(UiText.DynamicString("Article Inserted"))
      )
    }
  }
}