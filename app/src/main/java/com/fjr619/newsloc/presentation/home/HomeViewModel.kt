package com.fjr619.newsloc.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
): ViewModel() {
    var state = mutableStateOf(HomeState())
        private set

    val news = newsUseCases.getNews(
        sources = listOf("engadget","ars-technica","crypto-coins-news", "hacker-news","recode","techcrunch","techradar","the-next-web","the-verge","wired","")
    ).cachedIn(viewModelScope)
}