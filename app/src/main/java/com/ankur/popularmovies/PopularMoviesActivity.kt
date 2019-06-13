package com.ankur.popularmovies

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._http.MoviesApi
import com.ankur.popularmovies._http.MoviesClient
import com.ankur.popularmovies._mvi.MviLifecycle
import com.ankur.popularmovies._repository.Error
import com.ankur.popularmovies._repository.ErrorType
import com.ankur.popularmovies._repository.PopularMoviesRepository
import com.ankur.popularmovies._repository.PopularMoviesRepositoryImpl
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_popular_movies.movieRecyclerView
import kotlinx.android.synthetic.main.activity_popular_movies.movieSearchView
import kotlinx.android.synthetic.main.activity_popular_movies.progressBar
import kotlinx.android.synthetic.main.activity_popular_movies.rootLayout

const val KEY_MOVIES = "movies"

class PopularMoviesActivity : AppCompatActivity(), PopularMoviesView {
  private val compositeDisposable = CompositeDisposable()

  private lateinit var lifecycleEvent: MviLifecycle
  private val lifecycle = PublishSubject.create<MviLifecycle>()
  private val states = BehaviorSubject.create<PopularMoviesState>()

  private val searchQueryChanges = PublishSubject.create<String>()
  private val retryClicks = PublishSubject.create<Unit>()
  private val intentions by lazy { PopularMoviesIntentions(searchQueryChanges, retryClicks) }

  private val moviesApi = MoviesClient.getInstance().create(MoviesApi::class.java)

  private val repository: PopularMoviesRepository by lazy { PopularMoviesRepositoryImpl(moviesApi) }
  private val movieAdapter = MoviesAdapter(arrayListOf())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_popular_movies)

    lifecycleEvent = if (savedInstanceState == null) MviLifecycle.CREATED else MviLifecycle.RESTORED

    savedInstanceState?.let {
      if (savedInstanceState.getParcelable<PopularMoviesState>(KEY_MOVIES) != null)
        states.onNext(savedInstanceState.getParcelable(KEY_MOVIES))
    }

    setup()
  }

  private fun setup() {
    movieRecyclerView.layoutManager = LinearLayoutManager(this)
    movieRecyclerView.adapter = movieAdapter

    movieSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
          searchQueryChanges.onNext(it)
        }
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
          searchQueryChanges.onNext(it)
        }
        return true
      }
    })
  }

  override fun onStart() {
    super.onStart()
    PopularMoviesModel
      .bind(lifecycle, repository, intentions, states)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        println(it)
        states.onNext(it)
        render(it)
      }
      .addTo(compositeDisposable)

    lifecycle.onNext(lifecycleEvent)
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState)
    outState?.putParcelable(KEY_MOVIES, states.value)
  }

  override fun onStop() {
    super.onStop()
    compositeDisposable.clear()
  }

  override fun showResults(movies: List<Movie>) {
    movieAdapter.updateMovies(movies)
  }

  override fun showProgress(show: Boolean) {
    progressBar.visibility = if (show) View.VISIBLE else View.GONE
  }

  override fun showError(error: Error) {
    val errorRes = when (error.type) {
      ErrorType.CONNECTION -> R.string.error_conncection
      ErrorType.UNKNOWN -> R.string.error_unknown
    }

    showSnackBar(errorRes)
  }

  private fun showSnackBar(@StringRes stringRes: Int) {
    Snackbar.make(rootLayout, stringRes, Snackbar.LENGTH_INDEFINITE)
      .setAction(R.string.action_retry) {
        retryClicks.onNext(Unit)
      }
      .show()
  }
}
