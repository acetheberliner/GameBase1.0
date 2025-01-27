package progetto.videogame.gamebase.network.search

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchGameApi {
    private const val BASE_URL = "https://api.igdb.com/v4/games/"
    private var retrofitService: SearchGameApiService? = null

    fun getRetrofitService(): SearchGameApiService {
        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            retrofitService = retrofit.create(SearchGameApiService::class.java)
        }
        return retrofitService!!
    }
}