package com.ankur.popularmovies._repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Error(
    val type: ErrorType
) : Parcelable

enum class ErrorType {
  CONNECTION,

  UNKNOWN;
}
