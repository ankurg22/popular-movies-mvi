package com.ankur.popularmovies

import android.app.Application
import com.ankur.popularmovies._di.AppComponent
import com.ankur.popularmovies._di.AppModule
import com.ankur.popularmovies._di.DaggerAppComponent

class MoviesApplication : Application() {
  private lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()
    appComponent = initialiseDagger()
  }

  private fun initialiseDagger(): AppComponent {
    return DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .build()
  }

  fun appComponent(): AppComponent = appComponent
}
