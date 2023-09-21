package com.fjr619.newsloc.presentation.home

data class HomeState(
    val newsTicker: String = "",
    val isLoading: Boolean = false,
)