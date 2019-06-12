package com.ankur.popularmovies

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_popular_movies.*

class PopularMoviesActivity : AppCompatActivity(), PopularMoviesView {
  private val compositeDisposable = CompositeDisposable()

  private val lifecycle = PublishSubject.create<MviLifecycle>()
  private lateinit var lifecycleEvent: MviLifecycle
  private val moviesApi = MoviesClient.getInstance().create(MoviesApi::class.java)
  private val searchIntention = PublishSubject.create<String>()
  private val intentions by lazy { PopularMoviesIntentions(searchIntention) }

  private val states = BehaviorSubject.create<PopularMoviesState>()

  private val movieAdapter = MoviesAdapter(arrayListOf())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_popular_movies)

    movieRecyclerView.layoutManager = LinearLayoutManager(this)
    movieRecyclerView.adapter = movieAdapter

    movieSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
          searchIntention.onNext(it)
        }
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
          searchIntention.onNext(it)
        }
        return true
      }
    })

    lifecycleEvent = MviLifecycle.CREATED
  }

  override fun onStart() {
    super.onStart()
    PopularMoviesModel
      .bind(lifecycle, moviesApi, intentions, states)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        println(it)
        states.onNext(it)
        render(it)
      }
      .addTo(compositeDisposable)

    lifecycle.onNext(lifecycleEvent)
  }

  override fun onStop() {
    super.onStop()
    compositeDisposable.clear()
  }

  override fun showResults(movies: List<Movie>) {
    movieAdapter.addData(movies)
    movieAdapter.notifyDataSetChanged()
  }

  override fun showProgress() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    progressBar.visibility = View.INVISIBLE
  }

  override fun showError(error: Error) {
    when (error.type) {
      ErrorType.CONNECTION -> {
        Snackbar.make(rootLayout, R.string.error_conncection, Snackbar.LENGTH_INDEFINITE)
          .setAction(R.string.action_retry) {
            // TODO()
          }
          .show()
      }
      ErrorType.UNKNOWN -> {
        Snackbar.make(rootLayout, R.string.error_unknown, Snackbar.LENGTH_INDEFINITE)
          .setAction(R.string.action_retry) {
            // TODO()
          }
          .show()
      }
    }
  }
}
