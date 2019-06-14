package com.ankur.popularmovies._db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MovieDao {
  @Query("DELETE FROM popular_movies")
  fun clearTable()
}