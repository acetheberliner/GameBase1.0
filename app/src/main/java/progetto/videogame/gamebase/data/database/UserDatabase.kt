package progetto.videogame.gamebase.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.provider.ContactsContract

import progetto.videogame.gamebase.data.entity.User
import progetto.videogame.gamebase.data.dao.UserDao

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(UserDatabaseCallback(context,scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class UserDatabaseCallback(
            private val context: Context,
            private val scope: CoroutineScope
        ): RoomDatabase.Callback(){
            override fun onOpen(db: SupportSQLiteDatabase){
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDB(database.userDao(),context)
                    }
                }
            }

            suspend fun populateDB(userDao: UserDao, context: Context){
                val contactsList = mutableListOf<User>()

                val cursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null
                )
                cursor?.use{
                    val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val phoneNumberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    if (nameIndex >= 0 && phoneNumberIndex >= 0) {
                        while (it.moveToNext()) {
                            val name = it.getString(nameIndex) ?: "N/A"
                            val phoneNumber = it.getString(phoneNumberIndex) ?: "N/A"

                            val user = User(name = name, phoneNumber = phoneNumber)
                            contactsList.add(user)
                        }
                    }
                }
                userDao.insertAll(contactsList)
            }
        }
    }
}