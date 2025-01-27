package progetto.videogame.gamebase.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import progetto.videogame.gamebase.data.entity.GuestSession
import progetto.videogame.gamebase.data.dao.GuestSessionDao

@Database(entities = [GuestSession::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun guestSessionDao(): GuestSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guest_session_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}