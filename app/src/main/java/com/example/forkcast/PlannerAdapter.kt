package com.example.forkcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forkcast.databinding.ItemPlannerBinding

class PlannerAdapter(private val plans: List<MealPlan>) :
    RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder>() {

    inner class PlannerViewHolder(val binding: ItemPlannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannerViewHolder {
        val binding = ItemPlannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlannerViewHolder, position: Int) {
        val plan = plans[position]
        holder.binding.mealType.text = plan.mealType
        holder.binding.mealName.text = plan.mealName
    }

    override fun getItemCount(): Int = plans.size
}
