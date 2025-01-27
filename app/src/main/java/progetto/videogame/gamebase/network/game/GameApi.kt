package progetto.videogame.gamebase.network.game

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GameApi {
    private const val BASE_URL = "https://api.igdb.com/v4/games"
    private var retrofitService: GameApiService? = null

    fun getRetrofitService(): GameApiService {
        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            retrofitService = retrofit.create(GameApiService::class.java)
        }
        return retrofitService!!
    }
}