package progetto.videogame.gamebase.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import progetto.videogame.gamebase.R
import progetto.videogame.gamebase.data.entity.User
import progetto.videogame.gamebase.ui.viewmodel.GameViewModel

class SummaryActivity : AppCompatActivity() {

    private lateinit var backgroundImageView: ImageView
    private lateinit var priceTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var proceedButton: Button
    private lateinit var shareButton: Button
    private lateinit var sentToTextView: TextView

    private var gameId: Int? = null

    private val apiKey = "ia9dgescdn74anr1iltzhvp6kteel7"

    private val gameViewModel: GameViewModel by viewModels {
        GameViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        backgroundImageView = findViewById(R.id.background_image)
        priceTextView = findViewById(R.id.price_text_view)
        titleTextView = findViewById(R.id.title_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)
        proceedButton = findViewById(R.id.proceed_button)
        shareButton = findViewById(R.id.share_button)
        sentToTextView = findViewById(R.id.sent_to_text_view)

        gameId = intent.getIntExtra("GAME_ID", -1)
        Log.d("SummaryActivity", "Received game ID: $gameId")

        if (gameId != null && gameId != -1) {
            gameId?.let {
                gameViewModel.fetchGameDetails(apiKey, it)
            }
        } else {
            Toast.makeText(this, "No game ID provided", Toast.LENGTH_SHORT).show()
            finish()
        }

        gameViewModel.gameDetails.observe(this) { gameDetails ->
            titleTextView.text = gameDetails.original_title
            descriptionTextView.text = gameDetails.overview
            priceTextView.text = "Price: $6.99"
            val imageUrl = "https://image.tmdb.org/t/p/w500${gameDetails.poster_path}"
            Glide.with(this@SummaryActivity).load(imageUrl).into(backgroundImageView)
        }

        proceedButton.setOnClickListener {
            val intent = Intent(this, ShippingActivity::class.java)
            startActivity(intent)
        }

        shareButton.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_USER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_USER && resultCode == RESULT_OK) {
            val selectedUser = data?.getSerializableExtra("selected_user") as? User
            selectedUser?.let {
                onUserSelected(it)
            }
        }
    }

    private fun onUserSelected(user: User) {
        val message = "Game sent to ${user.name}"
        sentToTextView.text = message
        sentToTextView.visibility = View.VISIBLE

        // send an intent to share the game with the selected user
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this game: ${titleTextView.text}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Game"))
    }

    companion object {
        private const val REQUEST_CODE_USER = 1
    }
}