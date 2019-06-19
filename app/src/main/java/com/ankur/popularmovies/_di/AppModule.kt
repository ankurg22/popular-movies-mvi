package com.ankur.popularmovies._di

import android.app.Application
import android.content.Context
import com.ankur.popularmovies._db.PopularMoviesDatabase
import com.ankur.popularmovies._http.MoviesApi
import com.ankur.popularmovies._http.MoviesClient
import com.ankur.popularmovies._repository.PopularMoviesRepository
import com.ankur.popularmovies._repository.PopularMoviesRepositoryImpl
import com.ankur.popularmovies._repository.SchedulerProvider
import com.ankur.popularmovies._repository.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

  @Provides
  @Singleton
  fun provideMoviesRepository(
      appDatabase: PopularMoviesDatabase,
      moviesApi: MoviesApi,
      schedulerProvider: SchedulerProvider
  ): PopularMoviesRepository =
      PopularMoviesRepositoryImpl(appDatabase, moviesApi, schedulerProvider)

  @Provides
  @Singleton
  fun provideMoviesDatabase(context: Context): PopularMoviesDatabase =
      PopularMoviesDatabase.getInstance(context).build()

  @Provides
  @Singleton
  fun provideContext(): Context = application

  @Provides
  @Singleton
  fun provideMoviesApi(): MoviesApi = MoviesClient.getInstance().create(MoviesApi::class.java)

  @Provides
  @Singleton
  fun provideSchedulerProvider(): SchedulerProvider {
    return SchedulerProviderImpl()
  }
}
