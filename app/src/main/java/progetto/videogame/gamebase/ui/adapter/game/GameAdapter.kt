package progetto.videogame.gamebase.ui.adapter.game

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import progetto.videogame.gamebase.R
import progetto.videogame.gamebase.models.game.TMDbMovie
import progetto.videogame.gamebase.ui.SignInActivity

class GameAdapter(private val onClick: (TMDbMovie) -> Unit) :
    RecyclerView.Adapter<GameAdapter.MovieViewHolder>() {

    private var movies = emptyList<TMDbMovie>()

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.game_title)
        val descriptionView: TextView = itemView.findViewById(R.id.game_description)
        val imageView: ImageView = itemView.findViewById(R.id.game_image)
        val buyButton: Button = itemView.findViewById(R.id.buy_button)
        val movieItemLayout: View = itemView.findViewById(R.id.game_item_layout)

        fun bind(movie: TMDbMovie) {
            titleView.text = movie.original_title
            descriptionView.text = movie.overview

            Glide.with(imageView.context).load("https://image.tmdb.org/t/p/w185${movie.poster_path}")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView)

            // Listener for the "Buy" button
            buyButton.setOnClickListener {
                openLoginActivity(movie.id)
            }

            // Listener for the entire item layout
            movieItemLayout.setOnClickListener {
                openLoginActivity(movie.id)
            }

        }

        private fun openLoginActivity(movieId: Int) {
            Log.d("MovieAdapter", "Passing movie ID: $movieId to LoginActivity")
            //val context = itemView.context
            val intent = Intent(itemView.context, SignInActivity::class.java).apply {
                putExtra("MOVIE_ID", movieId)
            }
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val current = movies[position]
        holder.bind(current)
    }

    override fun getItemCount() = movies.size

    fun submitList(list: List<TMDbMovie>) {
        movies = list
        notifyDataSetChanged()
    }
}