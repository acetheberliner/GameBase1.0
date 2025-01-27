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
import progetto.videogame.gamebase.models.game.IGDbGame
import progetto.videogame.gamebase.ui.SignInActivity

class GameAdapter(private val onClick: (IGDbGame) -> Unit) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    private var games = emptyList<IGDbGame>()

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.game_title)
        val descriptionView: TextView = itemView.findViewById(R.id.game_description)
        val imageView: ImageView = itemView.findViewById(R.id.game_image)
        val buyButton: Button = itemView.findViewById(R.id.buy_button)
        val gameItemLayout: View = itemView.findViewById(R.id.game_item_layout)

        fun bind(game: IGDbGame) {
            titleView.text = game.original_title
            descriptionView.text = game.overview

            Glide.with(imageView.context).load("https://image.tmdb.org/t/p/w185${game.poster_path}")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView)

            // Listener for the "Buy" button
            buyButton.setOnClickListener {
                openLoginActivity(game.id)
            }

            // Listener for the entire item layout
            gameItemLayout.setOnClickListener {
                openLoginActivity(game.id)
            }

        }

        private fun openLoginActivity(gameId: Int) {
            Log.d("GameAdapter", "Passing game ID: $gameId to LoginActivity")
            //val context = itemView.context
            val intent = Intent(itemView.context, SignInActivity::class.java).apply {
                putExtra("GAME_ID", gameId)
            }
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val current = games[position]
        holder.bind(current)
    }

    override fun getItemCount() = games.size

    fun submitList(list: List<IGDbGame>) {
        games = list
        notifyDataSetChanged()
    }
}