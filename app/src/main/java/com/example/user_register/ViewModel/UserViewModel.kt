package com.example.user_register.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.user_register.Model.User
import com.example.user_register.Model.UserDataBase
import com.example.user_register.Model.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    // LiveData que contendrá la lista de usuarios
    val allUser: LiveData<List<User>>

    init {
        // Obtener el UserDao y construir el UserRepository
        val userDao = UserDataBase.getDatabase(application).getUserDao()
        repository = UserRepository(userDao)

        // Obtener la lista de usuarios desde el UserRepository
        allUser = repository.allUsers
    }

    // Método para insertar un nuevo usuario utilizando coroutines y el ViewModelScope
    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    // Método para eliminar un usuario utilizando coroutines y el ViewModelScope
    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }
}


