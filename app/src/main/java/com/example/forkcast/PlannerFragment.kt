package com.example.forkcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forkcast.databinding.FragmentPlannerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PlannerFragment : Fragment() {

    private var _binding: FragmentPlannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: AppDatabase
    private lateinit var adapter: PlannerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        adapter = PlannerAdapter(emptyList())

        binding.plannerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.plannerRecyclerView.adapter = adapter

        // Clear all plans
        binding.clearPlannerButton.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    database.mealPlanDao().clearAll()
                }
                loadPlans()
            }
        }

        // Load or generate random plans
        loadPlans()
    }

    private fun loadPlans() {
        lifecycleScope.launch {
            val plans = withContext(Dispatchers.IO) {
                database.mealPlanDao().getAllPlans()
            }

            if (plans.isEmpty()) {
                generateRandomPlans()
            } else {
                adapter = PlannerAdapter(plans)
                binding.plannerRecyclerView.adapter = adapter
            }
        }
    }

    private fun generateRandomPlans() {
        lifecycleScope.launch {
            val mealTypes = listOf("Breakfast", "Lunch", "Dinner")
            val randomMeals = listOf(
                "Pancakes", "Chicken Salad", "Beef Stew",
                "Spaghetti", "Fish & Chips", "Vegetable Stir Fry",
                "Curry Rice", "Tacos", "Burgers"
            )

            val newPlans = mealTypes.map {
                MealPlan(
                    id = 0,
                    mealType = it,
                    mealName = randomMeals[Random.nextInt(randomMeals.size)]
                )
            }

            withContext(Dispatchers.IO) {
                database.mealPlanDao().insertAll(newPlans)
            }

            loadPlans()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
