package com.ankur.popularmovies

import android.os.Parcelable
import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._repository.Error
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class PopularMoviesState(
  val fetchAction: FetchAction,
  val movies: @RawValue List<Movie>,
  val searchedMovies: @RawValue List<Movie>,
  val error: @RawValue Error?
) : Parcelable

enum class FetchAction {
  IN_PROGRESS,

  FETCH_SUCCESSFUL,

  FETCH_FAILED
}
