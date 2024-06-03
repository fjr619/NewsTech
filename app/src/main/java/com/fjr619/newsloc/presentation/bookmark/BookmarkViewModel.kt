package com.fjr619.newsloc.presentation.bookmark

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    private val _state = mutableStateOf(BookmarkState())
    val state: State<BookmarkState> = _state

    init {
        getArticles()
    }

    private fun getArticles() {
//        newsUseCases.getBookmarks().onEach {
//            _state.value = _state.value.copy(articles = it)
//        }.launchIn(viewModelScope)
        val articles = newsUseCases.getBookmarks().cachedIn(viewModelScope)
        _state.value = _state.value.copy(articles = articles)
    }
}