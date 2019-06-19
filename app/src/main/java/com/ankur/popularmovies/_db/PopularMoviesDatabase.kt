package com.ankur.popularmovies._db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ankur.popularmovies._http.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = true)
abstract class PopularMoviesDatabase : RoomDatabase() {
  abstract fun movieDao(): MovieDao
}
