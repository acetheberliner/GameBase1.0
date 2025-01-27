package progetto.videogame.gamebase

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

import progetto.videogame.gamebase.ui.SignInActivity
import progetto.videogame.gamebase.ui.adapter.game.GameAdapter
import progetto.videogame.gamebase.ui.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {

    private val gameViewModel: GameViewModel by viewModels {
        GameViewModel.Factory(application)
    }
    private val apiKey = "ia9dgescdn74anr1iltzhvp6kteel7" // <------------- MODFICA API KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val progressIndicator = findViewById<CircularProgressIndicator>(R.id.progressIndicator)
        val searchView = findViewById<SearchView>(R.id.search_view)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        val adapter = GameAdapter { game ->
            val intent = Intent(this, SignInActivity::class.java).apply {
                putExtra("GAME_ID", game.id) // <--------------------- MODIFICA CAMPO DATABASE (?)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (isNetworkAvailable()) {
                        progressIndicator.visibility = View.VISIBLE
                        gameViewModel.searchGames(apiKey, it) //  <--------------- INDAGA SE QUEL METODO é DEFINITO DALLA API o é PRESENTE ALTROVE NEL CODE
                    } else {
                        showNoInternetConnectionMessage()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        gameViewModel.games.observe(this, Observer { games ->
            progressIndicator.visibility = View.GONE
            games?.let { adapter.submitList(it) }
        })

        gameViewModel.searchResults.observe(this, Observer { searchResults ->
            progressIndicator.visibility = View.GONE
            searchResults?.let { adapter.submitList(it) }
        })

        handleNetworkOperations()

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun handleNetworkOperations() {
        if (isNetworkAvailable()) {
            gameViewModel.fetchTrendingMovies(apiKey)
        } else {
            showNoInternetConnectionMessage()
        }
    }

    private fun showNoInternetConnectionMessage() {
        Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val searchView = findViewById<SearchView>(R.id.search_view)
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
            gameViewModel.fetchTrendingMovies(apiKey)
        } else {
            super.onBackPressed()
        }
    }
}