package com.example.forkcast

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    suspend fun registerUser(name: String, email: String, password: String): Boolean {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) return false
        val hashed = AppDatabase.SecurityUtils.hashPassword(password)
        val newUser = User(name = name, email = email, passwordHash = hashed)
        userDao.insertUser(newUser)
        return true
    }

    suspend fun loginUser(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email) ?: return false
        val hashed = AppDatabase.SecurityUtils.hashPassword(password)
        return user.passwordHash == hashed
    }
}
