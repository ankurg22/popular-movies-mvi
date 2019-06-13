package com.ankur.popularmovies

data class FetchEvent<T>(
  val fetchAction: FetchAction,
  val result: T,
  val error: Error
)