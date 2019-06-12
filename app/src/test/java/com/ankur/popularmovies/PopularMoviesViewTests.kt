package com.ankur.popularmovies

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PopularMoviesViewTests {
    @Spy
    private lateinit var view: SpyablePopularMoviesView

    @Test
    fun `fetching in progress and UI is displaying progress bar`() {
        // act
        view.render(PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null))

        //assert
        verify(view).showProgress()
        verify(view, never()).showError(any())
        verify(view, never()).showResults(any())
        verify(view, never()).hideProgress()
    }

    @Test
    fun `fetching is successful and UI displays a list`() {
        // act
        val movies = listOf(
            Movie(1, "asd", "asda")
        )

        view.render(PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null))

        // assert
        verify(view).hideProgress()
        verify(view).showResults(movies)
        verify(view, never()).showError(any())
        verify(view, never()).showProgress()
    }
}