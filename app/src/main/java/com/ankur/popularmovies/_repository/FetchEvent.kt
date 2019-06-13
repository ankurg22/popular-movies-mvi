package com.ankur.popularmovies._repository

import com.ankur.popularmovies.FetchAction

data class FetchEvent<T>(
    val fetchAction: FetchAction,
    val result: T?,
    val error: Error?
)
