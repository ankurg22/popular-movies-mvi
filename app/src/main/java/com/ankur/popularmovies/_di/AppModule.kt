package com.ankur.popularmovies._di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ankur.popularmovies.BuildConfig
import com.ankur.popularmovies._db.PopularMoviesDatabase
import com.ankur.popularmovies._http.MoviesApi
import com.ankur.popularmovies._http.RxThreadCallAdapterFactory
import com.ankur.popularmovies._repository.PopularMoviesRepository
import com.ankur.popularmovies._repository.PopularMoviesRepositoryImpl
import com.ankur.popularmovies._repository.SchedulerProvider
import com.ankur.popularmovies._repository.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val DATABASE_NAME = "movies"

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
  fun provideMoviesDatabase(context: Context): PopularMoviesDatabase {
    return Room
        .databaseBuilder(
            context,
            PopularMoviesDatabase::class.java,
            DATABASE_NAME
        )
        .build()
  }

  @Provides
  @Singleton
  fun provideContext(): Context = application

  @Provides
  @Singleton
  fun provideMoviesApi(retrofit: Retrofit): MoviesApi {
    return retrofit
        .create(MoviesApi::class.java)
  }

  @Provides
  @Singleton
  fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(RxThreadCallAdapterFactory())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().build())
        .build()
  }

  @Provides
  @Singleton
  fun provideSchedulerProvider(): SchedulerProvider {
    return SchedulerProviderImpl()
  }
}
