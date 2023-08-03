package com.example.user_register.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameUsername: String,
    val fullName: String,
    val age: Int
)
