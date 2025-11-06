package com.example.forkcast

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(meal: FavoriteMeal)

    @Delete
    suspend fun removeFavorite(meal: FavoriteMeal)

    @Query("SELECT * FROM favorite_meals")
    suspend fun getAllFavorites(): List<FavoriteMeal>

    @Query("SELECT * FROM favorite_meals WHERE idMeal = :id")
    suspend fun getFavoriteById(id: String): FavoriteMeal?
}
