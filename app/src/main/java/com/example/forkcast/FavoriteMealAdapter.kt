// FavoriteMealAdapter.kt
package com.example.forkcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forkcast.databinding.ItemRecipeBinding

class FavoriteMealAdapter(
    private val meals: List<FavoriteMeal>,
    private val onClick: (FavoriteMeal) -> Unit
) : RecyclerView.Adapter<FavoriteMealAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val meal = meals[position]
        holder.binding.recipeTitle.text = meal.name
        Glide.with(holder.itemView.context).load(meal.thumbnail).into(holder.binding.recipeImage)

        holder.itemView.setOnClickListener { onClick(meal) }
    }

    override fun getItemCount() = meals.size
}
