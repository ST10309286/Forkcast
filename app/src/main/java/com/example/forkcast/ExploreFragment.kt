package com.example.forkcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forkcast.databinding.FragmentExploreBinding
import com.example.forkcast.model.RecipeItem
import com.example.forkcast.network.RetrofitInstance
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            try {
                // ✅ Fetch meals from TheMealDB API
                val response = RetrofitInstance.api.getMealsByCategory("Seafood")
                val meals = response?.meals ?: emptyList()

                if (meals.isNotEmpty()) {
                    // ✅ Convert Meal → RecipeItem
                    val recipeItems = meals.map { meal ->
                        RecipeItem(
                            id = meal.idMeal.toInt(),
                            title = meal.strMeal,
                            image = meal.strMealThumb
                        )
                    }

                    // ✅ Use converted list
                    binding.recyclerView.adapter = RecipeAdapter(recipeItems) { selectedRecipe ->
                        val action = ExploreFragmentDirections
                            .actionExploreToRecipeDetail(selectedRecipe.id)
                        findNavController().navigate(action)
                    }

                } else {
                    Toast.makeText(requireContext(), "No meals found!", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
