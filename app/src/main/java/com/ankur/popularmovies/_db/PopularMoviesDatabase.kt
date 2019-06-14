package com.ankur.popularmovies._db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ankur.popularmovies._http.Movie

const val DATABASE_NAME = "movies"

@Database(entities = [Movie::class], version = 1)
abstract class PopularMovieDatabase : RoomDatabase() {
  abstract fun movieDao(): MovieDao

  fun getInstance(context: Context): RoomDatabase {
    return Room
        .databaseBuilder(
            context,
            PopularMovieDatabase::class.java,
            DATABASE_NAME
        )
        .build()
  }
}