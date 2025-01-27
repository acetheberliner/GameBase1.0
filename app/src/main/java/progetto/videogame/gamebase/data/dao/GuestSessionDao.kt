package progetto.videogame.gamebase.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import progetto.videogame.gamebase.data.entity.GuestSession

@Dao
interface GuestSessionDao {
    @Query("SELECT * FROM guest_session_table WHERE username = :username LIMIT 1")
    suspend fun getGuestSessionByUsername(username: String): GuestSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuestSession(guestSession: GuestSession)
}