package com.example.forkcast.model

data class RecipeResponse(
    val recipes: List<RecipeItem>?  // ✅ Use "recipes" (Spoonacular returns 'recipes', not 'results')
)
