package com.ankur.popularmovies._di

import android.content.Context
import com.ankur.popularmovies.MoviesApplication
import com.ankur.popularmovies._db.PopularMoviesDatabase
import com.ankur.popularmovies._http.MoviesApi
import com.ankur.popularmovies._repository.PopularMoviesRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
  fun moviesRepository(): PopularMoviesRepository
  fun moviesDatabase(): PopularMoviesDatabase
  fun moviesApi(): MoviesApi

  companion object{
    fun obtain(context:Context):AppComponent{
      return (context.applicationContext as MoviesApplication).appComponent()
    }
  }
}
