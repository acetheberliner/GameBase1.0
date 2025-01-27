package progetto.videogame.gamebase.repository

import progetto.videogame.gamebase.data.dao.UserDao
import progetto.videogame.gamebase.data.entity.User

class UserRepository(private val userDao: UserDao) {

    // Inserts a users list
    suspend fun insertAll(users: List<User>) {
        userDao.insertAll(users)
    }
}