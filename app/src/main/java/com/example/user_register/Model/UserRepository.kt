package com.example.user_register.Model

import androidx.lifecycle.LiveData
import com.example.user_register.Model.User
import com.example.user_register.Model.UserDao

class UserRepository(private val userDao: UserDao) {
    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}
