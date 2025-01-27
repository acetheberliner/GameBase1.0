package progetto.videogame.gamebase.repository

import android.content.Context

import progetto.videogame.gamebase.models.game.IGDbGame
import progetto.videogame.gamebase.network.game.GameApi
import progetto.videogame.gamebase.network.search.SearchGameApi

class GameRepository(private val context: Context) {

    suspend fun getTrendingGames(apiKey: String): List<IGDbGame> {
        return GameApi.getRetrofitService().getTrendingGames(apiKey).results
    }

    suspend fun searchGames(apiKey: String, query: String): List<IGDbGame> {
        return SearchGameApi.getRetrofitService().searchGames(apiKey, query).results
    }

    suspend fun getGameDetails(apiKey: String, gameId: Int): IGDbGame {
        return GameApi.getRetrofitService().getGameDetails(gameId, apiKey)
    }
}