package com.ankur.popularmovies._db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.ankur.popularmovies._http.Movie
import io.reactivex.Single

@Dao
interface MovieDao {
  @Transaction
  @Insert
  fun insertAll(movies: List<Movie>)

  @Query("SELECT * from popular_movies")
  fun getAll(): Single<List<Movie>>

  @Query("DELETE FROM popular_movies")
  fun clearTable()
}
