package com.fjr619.newsloc.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.util.DateUtils
import com.fjr619.newsloc.util.ResourceProvider
import com.fjr619.newsloc.util.pulltorefresh.PullToRefreshLayoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
    private val resourceProvider: ResourceProvider,
): ViewModel() {

//    var state by mutableStateOf(HomeState())

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

    private fun convertElapsedTimeIntoText(timeElapsed: Long): String {
        return DateUtils.getTimePassedInHourMinSec(resourceProvider, timeElapsed)
    }
}

