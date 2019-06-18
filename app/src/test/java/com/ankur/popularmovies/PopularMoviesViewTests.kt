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
}
