package com.fjr619.newsloc.util

sealed class UiEffect {

    data class Toast(val message: UiText): UiEffect()

    data class Dialog(val title: String, val message: String): UiEffect()

    data class None(val message: String? = null): UiEffect()

}