package com.example.forkcast

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.OnConflictStrategy

@Dao
interface MealPlanDao {
    @Insert
    suspend fun insert(plan: MealPlan)

    @Insert
    suspend fun insertAll(plans: List<MealPlan>)

    @Query("SELECT * FROM meal_plan")
    suspend fun getAllPlans(): List<MealPlan>

    @Query("DELETE FROM meal_plan")
    suspend fun clearAll()
}

