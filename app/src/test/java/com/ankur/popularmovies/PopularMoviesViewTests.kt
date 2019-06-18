package com.ankur.popularmovies

import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._repository.Error
import com.ankur.popularmovies._repository.ErrorType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PopularMoviesViewTests {
  @Spy private lateinit var view: SpyablePopularMoviesView

  @Test fun `cache empty, fetch from network and show normal loading`() {
    // act
    view.render(PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null))

    // assert
    verify(view).showProgress(true)
    verify(view, never()).showProgress(false)
    verify(view, never()).showSilentProgress(true)
    verify(view, never()).showSilentProgress(false)
    verify(view, never()).showError(any())
    verify(view, never()).showResults(any())
  }

  @Test fun `cache has data, show results while silent loading`() {
    // act
    val movies = listOf(
        Movie(1, "asd", "asda")
    )
    view.render(PopularMoviesState(FetchAction.IN_PROGRESS, movies, emptyList(), null))

    // assert
    verify(view).showResults(movies)
    verify(view).showSilentProgress(true)
    verify(view, never()).showSilentProgress(false)
    verify(view, never()).showProgress(true)
    verify(view, never()).showProgress(false)
    verify(view, never()).showError(any())
  }

  @Test fun `network fetching failed and UI displays an error`() {
    // act
    val error = Error(ErrorType.CONNECTION)
    view.render(PopularMoviesState(FetchAction.FETCH_FAILED, emptyList(), emptyList(), error))

    // assert
    verify(view).showProgress(false)
    verify(view).showSilentProgress(false)
    verify(view).showError(error)

    verify(view, never()).showResults(any())
    verify(view, never()).showProgress(true)
    verify(view, never()).showSilentProgress(true)
  }

  @Test fun `network fetching is successful and UI displays a list`() {
    // act
    val movies = listOf(
        Movie(1, "asd", "asda")
    )

    view.render(PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null))

    // assert
    verify(view).showProgress(false)
    verify(view).showSilentProgress(false)
    verify(view).showResults(movies)

    verify(view, never()).showError(any())
    verify(view, never()).showProgress(true)
    verify(view, never()).showSilentProgress(true)
  }

  @Test fun `user search by movie name and UI displays a list`() {
    // act
    val movies = listOf(
        Movie(2, "fdfas", "adfa"),
        Movie(2, "fdfas", "adfa"),
        Movie(2, "fdfas", "adfa")
    )
    val searchedMovies = listOf(
        Movie(2, "fdfas", "adfa")
    )
    view.render(PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, searchedMovies, null))

    // assert
    verify(view).showResults(searchedMovies)

    verify(view, never()).showProgress(true)
    verify(view, never()).showProgress(false)
    verify(view, never()).showSilentProgress(true)
    verify(view, never()).showSilentProgress(false)
    verify(view, never()).showError(any())
  }
}
