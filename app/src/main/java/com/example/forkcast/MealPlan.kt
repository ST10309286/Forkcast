package com.example.forkcast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plan")
data class MealPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateIso: String? = null,   // optional date (e.g. "2025-10-12"), useful later
    val mealType: String,          // e.g. "Breakfast", "Lunch", "Dinner"
    val mealName: String,
    val recipeId: Int? = null      // optional link to recipe
)
