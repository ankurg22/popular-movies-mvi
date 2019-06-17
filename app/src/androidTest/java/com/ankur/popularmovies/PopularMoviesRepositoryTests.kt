package com.ankur.popularmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ankur.popularmovies._db.MovieDao
import com.ankur.popularmovies._db.PopularMoviesDatabase
import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._http.MoviesApi
import com.ankur.popularmovies._http.MoviesResponse
import com.ankur.popularmovies._repository.Error
import com.ankur.popularmovies._repository.ErrorType
import com.ankur.popularmovies._repository.FetchEvent
import com.ankur.popularmovies._repository.PopularMoviesRepositoryImpl
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.net.SocketTimeoutException

@RunWith(AndroidJUnit4::class) class PopularMoviesRepositoryTests {
  @JvmField @Rule val instantTaskExecutor = InstantTaskExecutorRule()

  private lateinit var moviesApi: MoviesApi
  private lateinit var movieDao: MovieDao
  private lateinit var repository: PopularMoviesRepositoryImpl

  @Before fun setup() {
    val context = InstrumentationRegistry
      .getInstrumentation()
      .context

    moviesApi = mock(MoviesApi::class.java)

    val database = Room
      .inMemoryDatabaseBuilder(context, PopularMoviesDatabase::class.java)
      .build()

    movieDao = database.movieDao()

    val schedulerProvider = TestSchedulerProvider()
    repository = PopularMoviesRepositoryImpl(database, moviesApi, schedulerProvider)
  }

  // Cache - Missed (no data in the database), Network - Miss,
  @Test fun fetchingPopularMovies_cacheMiss_networkMiss_no_results() {
    // Setup
    val error = Error(ErrorType.CONNECTION)
    `when`(moviesApi.getTopRatedMovies())
      .thenReturn(Observable.error(SocketTimeoutException()))

    // Act
    val repoObserver = repository
      .fetchMovies()
      .test()

    val dbObserver = movieDao
      .getAll()
      .test()

    // Assert
    repoObserver
      .assertNoErrors()
      .assertResult(
        FetchEvent(FetchAction.IN_PROGRESS, emptyList()),
        FetchEvent(FetchAction.FETCH_FAILED, emptyList(), error)
      )
      .assertTerminated()

    dbObserver
      .assertResult(emptyList()) // List<T>, when
  }

  // Cache - Miss, Network - Hit
  @Test fun fetchingPopularMovies_cacheMiss_networkHit_storesDataInCache() {
    // Setup
    `when`(moviesApi.getTopRatedMovies())
      .thenReturn(Observable.just(MoviesResponse(movies)))

    // Act
    val repoObserver = repository
      .fetchMovies()
      .test()

    val dbObserver = movieDao
      .getAll()
      .test()

    // Assert
    repoObserver
      .assertNoErrors()
      .assertResult(
        FetchEvent(FetchAction.IN_PROGRESS, emptyList()),
        FetchEvent(FetchAction.FETCH_SUCCESSFUL, movies)
      )
      .assertTerminated()

    dbObserver
      .assertNoErrors()
      .assertResult(movies)
  }


  // Cache - Hit, Network - Miss,
  @Test fun fetchingPopularMovies_cacheHit_networkMiss() {
    // Setup
    val error = Error(ErrorType.CONNECTION)
    `when`(moviesApi.getTopRatedMovies())
      .thenReturn(Observable.error(SocketTimeoutException()))

    movieDao.insertAll(movies)

    val repoObserver = repository
      .fetchMovies()
      .test()

    val dbObserver = movieDao
      .getAll()
      .test()

    // Assert
    repoObserver
      .assertNoErrors()
      .assertResult(
        FetchEvent(FetchAction.IN_PROGRESS, movies),
        FetchEvent(FetchAction.FETCH_FAILED, emptyList(), error)
      )
      .assertTerminated()

    dbObserver
      .assertNoErrors()
      .assertResult(movies)
  }

  // Cache - Hit, Network - Hit
  @Test fun fetchingMovies_cacheHit_networkHit_storesDataInCache() {
    // Setup
    `when`(moviesApi.getTopRatedMovies())
      .thenReturn(Observable.just(MoviesResponse(movies)))

    movieDao.insertAll(movies)

    // Act
    val repoObserver = repository
      .fetchMovies()
      .test()

    val dbObserver = movieDao
      .getAll()
      .test()

    // Assert
    repoObserver
      .assertNoErrors()
      .assertResult(
        FetchEvent(FetchAction.IN_PROGRESS, movies),
        FetchEvent(FetchAction.FETCH_SUCCESSFUL, movies)
      )
      .assertTerminated()

    dbObserver
      .assertNoErrors()
      .assertResult(movies)
  }
}

// Data
val movies = listOf(
  Movie(1, "XYZ", "292039"),
  Movie(2, "ABC", "292992")
)
