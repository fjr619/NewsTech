package com.fjr619.newsloc.util

sealed class UIComponent {

    data class Toast(val message: String): UIComponent()

    data class Dialog(val title: String, val message: String): UIComponent()

    data class None(val message: String? = null): UIComponent()

}