package progetto.videogame.gamebase.network.game

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class GameRequest(
    val fields: List<String>,
    val search: String
)

data class GameResponse(
    val id: Int,
    val name: String,
    val cover: Map<String, String>,
    val genres: List<String>
)

interface GameApiService {

    @Headers(
        "Client-ID: ia9dgescdn74anr1iltzhvp6kteel7",
        "Authorization: Bearer h883lebjc6mr5xiebkofz8dbv2rv5n"  // Bearer token
    )
    @POST("https://api.igdb.com/v4/games")
    fun searchGames(@Body request: GameRequest): Call<List<GameResponse>>
}
