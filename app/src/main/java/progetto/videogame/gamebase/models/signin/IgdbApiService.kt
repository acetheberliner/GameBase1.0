package progetto.videogame.gamebase.models.signin

import retrofit2.http.GET
import retrofit2.http.Query

interface IgdbApiService {

    @GET("authentication/guest_session/new")
    suspend fun createGuestSession(@Query("api_key") apiKey: String): GuestSessionResponse
}