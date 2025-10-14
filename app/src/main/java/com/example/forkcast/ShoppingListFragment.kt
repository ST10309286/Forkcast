package com.example.forkcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.forkcast.databinding.FragmentShoppingListBinding
import kotlinx.coroutines.launch

class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var adapter: ShoppingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()

        // Setup RecyclerView
        binding.shoppingRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Load data from Room
        loadShoppingList()

        // Clear all button
        binding.clearButton.setOnClickListener {
            lifecycleScope.launch {
                db.shoppingListDao().clearAll()
                loadShoppingList()
                Toast.makeText(requireContext(), "Shopping list cleared!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun loadShoppingList() {
        lifecycleScope.launch {
            val items = db.shoppingListDao().getAllItems()
            adapter = ShoppingAdapter(items)
            binding.shoppingRecyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
