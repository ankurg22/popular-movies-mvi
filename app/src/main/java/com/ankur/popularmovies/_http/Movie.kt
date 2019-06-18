package com.ankur.popularmovies._http

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "popular_movies")
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    val title: String,

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    val poster: String
) : Parcelable

@Parcelize
data class MoviesResponse(
    @SerializedName("results")
    val movies: List<Movie>
) : Parcelable
