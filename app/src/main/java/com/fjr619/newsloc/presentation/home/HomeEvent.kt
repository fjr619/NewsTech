package com.fjr619.newsloc.presentation.home

sealed class HomeEvent {

  data object GetArticles: HomeEvent()
}