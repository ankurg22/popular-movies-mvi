package com.ankur.popularmovies._repository

data class Error(
    val type: ErrorType
)

enum class ErrorType {
  CONNECTION,

  UNKNOWN;
}
