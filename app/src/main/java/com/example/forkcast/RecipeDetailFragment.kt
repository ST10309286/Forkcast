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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        // Initialize Room database
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()

        // Get recipe ID from navigation arguments
        val recipeId = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipeId.toString()

        lifecycleScope.launch {
            try {
                // Fetch recipe details from TheMealDB API
                val response = RetrofitInstance.api.getMealDetails(recipeId)
                val mealData = response?.meals?.firstOrNull()

                if (mealData != null) {
                    // Bind data to UI
                    binding.recipeTitle.text = mealData.strMeal
                    binding.recipeInstructions.text = mealData.strInstructions
                    Glide.with(requireContext()).load(mealData.strMealThumb).into(binding.recipeImage)

                    // Add ingredients to shopping list
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

                                Toast.makeText(
                                    requireContext(),
                                    "Added to shopping list!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to add: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
