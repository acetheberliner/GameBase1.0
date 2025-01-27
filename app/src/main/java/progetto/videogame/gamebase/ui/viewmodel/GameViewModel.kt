package progetto.videogame.gamebase.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import progetto.videogame.gamebase.models.game.IGDbGame
import progetto.videogame.gamebase.repository.GameRepository

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GameRepository = GameRepository(application)

    // LiveData for trending games
    private val _games = MutableLiveData<List<IGDbGame>>()
    val games: LiveData<List<IGDbGame>> = _games

    // LiveData for search results
    private val _searchResults = MutableLiveData<List<IGDbGame>>()
    val searchResults: LiveData<List<IGDbGame>> = _searchResults

    // LiveData for game details
    private val _gameDetails = MutableLiveData<IGDbGame>()
    val gameDetails: LiveData<IGDbGame> = _gameDetails

    fun fetchTrendingGames(apiKey: String) = viewModelScope.launch {
        val gameList = repository.getTrendingGames(apiKey)
        _games.postValue(gameList)
    }

    fun searchGames(apiKey: String, query: String) = viewModelScope.launch {
        val gameList = repository.searchGames(apiKey, query)
        _searchResults.postValue(gameList)
    }

    fun fetchGameDetails(apiKey: String, gameId: Int) = viewModelScope.launch {
        val game = repository.getGameDetails(apiKey, gameId)
        _gameDetails.postValue(game)
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}