package com.ankur.popularmovies

import android.app.Application
import com.ankur.popularmovies._di.AppComponent
import com.ankur.popularmovies._di.AppModule
import com.ankur.popularmovies._di.DaggerAppComponent

class MoviesApplication : Application() {
  companion object {
    private lateinit var appComponent: AppComponent
    fun appComponent(): AppComponent = appComponent
  }

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .build()
  }
}
