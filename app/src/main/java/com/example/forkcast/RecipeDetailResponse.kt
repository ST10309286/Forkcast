package com.example.forkcast

data class RecipeDetailResponse(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String?,
    val instructions: String?,
    val extendedIngredients: List<ExtendedIngredient> = emptyList()
)

data class ExtendedIngredient(
    val id: Int?,
    val name: String,
    val amount: Double?,
    val unit: String?
)
