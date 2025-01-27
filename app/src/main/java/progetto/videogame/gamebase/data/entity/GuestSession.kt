package progetto.videogame.gamebase.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "guest_session_table")
data class GuestSession(
    @PrimaryKey val username: String,
    val password: String,
    val guestSessionId: String
)