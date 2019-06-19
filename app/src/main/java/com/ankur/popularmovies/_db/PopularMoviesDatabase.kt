package com.ankur.popularmovies._db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ankur.popularmovies._http.Movie

const val DATABASE_NAME = "movies"

@Database(entities = [Movie::class], version = 1, exportSchema = true)
abstract class PopularMoviesDatabase : RoomDatabase() {
  abstract fun movieDao(): MovieDao

  companion object {
    fun getInstance(context: Context): RoomDatabase.Builder<PopularMoviesDatabase> {
      return Room
        .databaseBuilder(
          context,
          PopularMoviesDatabase::class.java,
          DATABASE_NAME
        )
    }
  }
}
