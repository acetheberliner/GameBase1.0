package progetto.videogame.gamebase.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import progetto.videogame.gamebase.models.game.TMDbMovie
import progetto.videogame.gamebase.repository.MovieRepository

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository = MovieRepository(application)

    // LiveData for trending movies
    private val _movies = MutableLiveData<List<TMDbMovie>>()
    val movies: LiveData<List<TMDbMovie>> = _movies

    // LiveData for search results
    private val _searchResults = MutableLiveData<List<TMDbMovie>>()
    val searchResults: LiveData<List<TMDbMovie>> = _searchResults

    // LiveData for movie details
    private val _movieDetails = MutableLiveData<TMDbMovie>()
    val movieDetails: LiveData<TMDbMovie> = _movieDetails

    fun fetchTrendingMovies(apiKey: String) = viewModelScope.launch {
        val movieList = repository.getTrendingMovies(apiKey)
        _movies.postValue(movieList)
    }

    fun searchMovies(apiKey: String, query: String) = viewModelScope.launch {
        val movieList = repository.searchMovies(apiKey, query)
        _searchResults.postValue(movieList)
    }

    fun fetchMovieDetails(apiKey: String, movieId: Int) = viewModelScope.launch {
        val movie = repository.getMovieDetails(apiKey, movieId)
        _movieDetails.postValue(movie)
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}