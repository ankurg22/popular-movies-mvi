package com.ankur.popularmovies._db

import androidx.room.Dao
import androidx.room.Query
import com.ankur.popularmovies._http.Movie
import io.reactivex.Single

@Dao
interface MovieDao {
  @Query("SELECT * from popular_movies")
  fun getAll(): Single<List<Movie>>

  @Query("DELETE FROM popular_movies")
  fun clearTable()
}