package progetto.videogame.gamebase.network.search

import progetto.videogame.gamebase.models.game.IGDbGame

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchGameApiService {
    @GET("search/game")
    suspend fun searchGames(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): SearchGameApiService
}

data class SearchGameResponse(
    val results: List<IGDbGame>
)