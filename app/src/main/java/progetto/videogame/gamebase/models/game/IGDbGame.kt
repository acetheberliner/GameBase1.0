package progetto.videogame.gamebase.models.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IGDbGame(
    val id: Int,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double
) : Parcelable {
//    val posterUrl: String
//        get() = "https://image.tmdb.org/t/p/w92/$poster_path"
}