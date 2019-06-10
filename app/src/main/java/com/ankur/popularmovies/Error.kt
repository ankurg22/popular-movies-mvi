package com.ankur.popularmovies

data class Error(
    val type: ErrorType
)

enum class ErrorType {
  CONNECTION,

  UNKNOWN;
}
