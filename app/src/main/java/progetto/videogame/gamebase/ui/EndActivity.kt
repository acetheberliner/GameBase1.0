package progetto.videogame.gamebase.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import progetto.videogame.gamebase.MainActivity
import progetto.videogame.gamebase.R

class EndActivity : AppCompatActivity() {

    private lateinit var actionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        actionButton = findViewById(R.id.action_button)

        // Load the image into the ImageView
        val backgroundImageView = findViewById<ImageView>(R.id.background_end_img)
        Glide.with(this)
            .load(R.drawable.end)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(backgroundImageView)

        // Set up the button click listener
        actionButton.setOnClickListener {
            // Navigate to the Game List Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //finish()
        }
    }
}