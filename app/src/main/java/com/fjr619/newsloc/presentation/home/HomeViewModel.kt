package com.fjr619.newsloc.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.presentation.common.pulltorefresh.PullToRefreshLayoutState
import com.fjr619.newsloc.util.DateUtils
import com.fjr619.newsloc.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
    private val resourceProvider: ResourceProvider,
): ViewModel() {

    var state by mutableStateOf(HomeState())

    val pullToRefreshState = PullToRefreshLayoutState(
        onTimeUpdated = { timeElapsed ->
            convertElapsedTimeIntoText(timeElapsed)
        },
    )

    private val _lastRefreshTime = pullToRefreshState.lastRefreshTime

    @OptIn(ExperimentalCoroutinesApi::class)
    val news =
        _lastRefreshTime.flatMapLatest { _ ->
            newsUseCases.getNews(
                sources = listOf("engadget","ars-technica","crypto-coins-news", "hacker-news","recode","techcrunch","techradar","the-next-web","the-verge","wired","")
            )
        }.cachedIn(viewModelScope)

    fun convertElapsedTimeIntoText(timeElapsed: Long): String {
        return DateUtils.getTimePassedInHourMinSec(resourceProvider, timeElapsed)
    }
}

