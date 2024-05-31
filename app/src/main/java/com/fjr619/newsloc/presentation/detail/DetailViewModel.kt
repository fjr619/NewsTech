package com.fjr619.newsloc.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.presentation.common.NewsSnackbarVisual
import com.fjr619.newsloc.util.composestateevents.consumed
import com.fjr619.newsloc.util.composestateevents.triggered
import com.fjr619.newsloc.util.snackbar.SnackbarMessage
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
) : ViewModel() {

  private var _viewState = MutableStateFlow(DetailViewState())
  val viewState = _viewState.asStateFlow()

  init {
    Log.e("TAG", "init detail viewmodel")
  }

  private suspend fun getBookmarkArticle(article: Article?) {
    dismissSnackbar()
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

  fun dismissSnackbar() = _viewState.update { it.copy(snackbarMessage = null) }

  private suspend fun deleteArticle(article: Article) {
    newsUseCases.deleteArticle(article = article)
    _viewState.update {
      it.copy(
        bookmark = null,
        snackbarMessage = SnackbarMessage.from(
          snackbarVisuals = NewsSnackbarVisual(message = "Article deleted"),
          onSnackbarResult = {}
        ),
      )
    }
  }

  private suspend fun upsertArticle(article: Article) {
    newsUseCases.upsertArticle(article = article)
    _viewState.update {
      it.copy(
        bookmark = article,
        snackbarMessage = SnackbarMessage.from(
          snackbarVisuals = NewsSnackbarVisual(message = "Article inserted"),
          onSnackbarResult = {}
        ),      )
    }
  }
}