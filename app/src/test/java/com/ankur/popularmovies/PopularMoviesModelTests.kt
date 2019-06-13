package com.ankur.popularmovies

import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._mvi.MviLifecycle
import com.ankur.popularmovies._repository.Error
import com.ankur.popularmovies._repository.ErrorType
import com.ankur.popularmovies._repository.FetchEvent
import com.ankur.popularmovies._repository.PopularMoviesRepository
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.net.SocketTimeoutException

class PopularMoviesModelTests {
  private lateinit var lifecycle: PublishSubject<MviLifecycle>
  private lateinit var moviesRepository: PopularMoviesRepository

  private lateinit var searchQueryChanges: PublishSubject<String>
  private lateinit var retryClicks: PublishSubject<Unit>
  private lateinit var intentions: PopularMoviesIntentions
  private lateinit var states: PublishSubject<PopularMoviesState>
  private lateinit var observer: TestObserver<PopularMoviesState>

  @Before fun setup() {
    lifecycle = PublishSubject.create()
    moviesRepository = mock(PopularMoviesRepository::class.java)

    searchQueryChanges = PublishSubject.create()
    retryClicks = PublishSubject.create()
    intentions = PopularMoviesIntentions(searchQueryChanges, retryClicks)

    states = PublishSubject.create<PopularMoviesState>()

    observer = PopularMoviesModel
      .bind(lifecycle, moviesRepository, intentions, states)
      .doOnNext { states.onNext(it) }
      .test()
  }

  @Test fun `user sees an error when fetching movies fails`() {
    // Setup
    `when`(moviesRepository.fetchMovies())
      .thenReturn(Observable.error(SocketTimeoutException()))

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)

    // Assert
    val error = Error(ErrorType.CONNECTION)

    observer.assertNoErrors()
    observer.assertValues(
      PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
      PopularMoviesState(FetchAction.FETCH_FAILED, emptyList(), emptyList(), error)
    )
  }

  @Test fun `user sees a list of movies when fetching movies succeeds`() {
    // Setup
    val movies = listOf(
        Movie(1, "abc", "cde"),
        Movie(2, "abc", "cde")
    )
    val fetchEvent: FetchEvent<List<Movie>> =
        FetchEvent(FetchAction.FETCH_SUCCESSFUL, movies, null)
    `when`(moviesRepository.fetchMovies())
      .thenReturn(Observable.just(fetchEvent))

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)

    // Assert
    observer.assertNoErrors()
    observer.assertValues(
      PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
      PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null)
    )
  }

  @Test fun `user search the movie by name and search succeeds`() {
    // Setup
    val movies = listOf(
        Movie(1, "Race 3", "cde"),
        Movie(2, "abc", "cde")
    )
    val filteredMovies = listOf(
        Movie(1, "Race 3", "cde")
    )

    val fetchEvent: FetchEvent<List<Movie>> =
        FetchEvent(FetchAction.FETCH_SUCCESSFUL, movies, null)
    `when`(moviesRepository.fetchMovies())
      .thenReturn(Observable.just(fetchEvent))

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)
    searchQueryChanges.onNext("Race 3")

    // Assert
    observer.assertNoErrors()
      .assertValues(
        PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
        PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null),
        PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, filteredMovies, null)
      )
  }

  @Test fun `user hits retry loading movies after error`() {
    // setup
    val movies = listOf(
        Movie(1, "Race 3", "cde"),
        Movie(2, "abc", "cde")
    )
    val fetchEvent: FetchEvent<List<Movie>> =
        FetchEvent(FetchAction.FETCH_SUCCESSFUL, movies, null)
    `when`(moviesRepository.fetchMovies())
      .thenReturn(Observable.error(SocketTimeoutException()))
      .thenReturn(Observable.just(fetchEvent))

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)
    retryClicks.onNext(Unit)

    // Assert
    val error = Error(ErrorType.CONNECTION)

    observer.assertNoErrors()
      .assertValues(
        PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
        PopularMoviesState(FetchAction.FETCH_FAILED, emptyList(), emptyList(), error),
        PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
        PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null)
      )
  }

  @Test
  fun `user sees movie list without loading again when UI is restored`() {
    // Setup
    val movies = listOf(
        Movie(1, "Race 3", "cde"),
        Movie(2, "abc", "cde")
    )
    val fetchEvent: FetchEvent<List<Movie>> =
        FetchEvent(FetchAction.FETCH_SUCCESSFUL, movies, null)
    `when`(moviesRepository.fetchMovies())
      .thenReturn(Observable.just(fetchEvent))

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)
    lifecycle.onNext(MviLifecycle.RESTORED)

    // Assert
    observer.assertNoErrors()
      .assertValues(
        PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
        PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null),
        PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null)
      )
      .assertNotTerminated()
  }
}
