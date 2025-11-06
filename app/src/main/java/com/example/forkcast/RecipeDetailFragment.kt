package com.example.forkcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.forkcast.databinding.FragmentRecipeDetailBinding
import com.example.forkcast.network.RetrofitInstance
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private var isFavorite = false
    private var currentMealId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        // Initialize Room DB
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "forkcast_db"
        ).build()

        // Get recipe ID
        val recipeId = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipeId.toString()
        currentMealId = recipeId

        lifecycleScope.launch {
            try {
                // Fetch from API
                val response = RetrofitInstance.api.getMealDetails(recipeId)
                val mealData = response?.meals?.firstOrNull()

                if (mealData != null) {
                    // Bind UI
                    binding.recipeTitle.text = mealData.strMeal
                    binding.recipeInstructions.text = mealData.strInstructions
                    Glide.with(requireContext()).load(mealData.strMealThumb).into(binding.recipeImage)

                    // ✅ Check if meal is favorite
                    val favorite = db.favoriteMealDao().getFavoriteById(mealData.idMeal)
                    isFavorite = favorite != null
                    updateFavoriteButtonText()

                    // ✅ Handle Favorite button
                    binding.btnFavorite.setOnClickListener {
                        lifecycleScope.launch {
                            toggleFavorite(mealData)
                        }
                    }

                    // ✅ Handle Add to Shopping List
                    binding.addToShoppingListButton.setOnClickListener {
                        lifecycleScope.launch {
                            try {
                                val ingredients = listOfNotNull(
                                    mealData.strIngredient1,
                                    mealData.strIngredient2,
                                    mealData.strIngredient3,
                                    mealData.strIngredient4,
                                    mealData.strIngredient5
                                ).filter { it.isNotBlank() }

                                for (ingredient in ingredients) {
                                    db.shoppingListDao().insertItem(
                                        ShoppingItem(
                                            name = ingredient,
                                            quantity = "",
                                            recipeId = mealData.idMeal.toInt()
                                        )
                                    )
                                }

                                Toast.makeText(requireContext(), "Added to shopping list!", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Failed to add: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), "Meal not found", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private suspend fun toggleFavorite(meal: com.example.forkcast.Meal) {
        val dao = db.favoriteMealDao()
        if (isFavorite) {
            dao.removeFavorite(FavoriteMeal(meal.idMeal, meal.strMeal, meal.strMealThumb))
            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            dao.addFavorite(FavoriteMeal(meal.idMeal, meal.strMeal, meal.strMealThumb))
            Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
        }
        isFavorite = !isFavorite
        updateFavoriteButtonText()
    }

    private fun updateFavoriteButtonText() {
        binding.btnFavorite.text = if (isFavorite) "Remove from Favorites" else "Add to Favorites"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
