package com.ankur.popularmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_movie.view.*

class MoviesAdapter(private val movies: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_movie, parent, false)
    return MoviesViewHolder(view)
  }

  override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
    val movie = movies[position]
    holder.bindMovie(movie)
  }

  override fun getItemCount(): Int {
    return movies.size
  }

  fun addData(movies: List<Movie>) {
    this.movies.clear()
    this.movies.addAll(movies)
  }

  class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindMovie(movie: Movie) {
      itemView.movieNameTextView.text = movie.title
      Picasso
          .get()
          .load(BASE_IMAGE_URL + movie.poster)
          .into(itemView.posterImageView)
    }
  }
}
