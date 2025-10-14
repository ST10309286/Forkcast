package com.example.forkcast
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: String?,
    val recipeId: Int?, // optional link to the recipe it came from
    val isChecked: Boolean = false
)
