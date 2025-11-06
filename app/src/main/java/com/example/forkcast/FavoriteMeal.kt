// FavoriteMeal.kt
package com.example.forkcast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_meals")
data class FavoriteMeal(
    @PrimaryKey val idMeal: String,
    val name: String,
    val thumbnail: String
)
