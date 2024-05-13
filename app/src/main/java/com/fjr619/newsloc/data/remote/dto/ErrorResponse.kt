package com.fjr619.newsloc.data.remote.dto

data class ErrorResponse(
  val status: String,
  val code: String,
  val message: String
)