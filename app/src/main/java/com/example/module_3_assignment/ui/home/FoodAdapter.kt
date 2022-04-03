package com.example.module_3_assignment.ui.home

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.ItemFoodBinding
import com.example.module_3_assignment.model.FoodEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class FoodAdapter(private val viewModel: FoodViewModel,private val listnere: IHome): androidx.recyclerview.widget.ListAdapter<Food, FoodAdapter.FoodViewHolder>(
    DiffCallBack
) {

    class FoodViewHolder(val binding: ItemFoodBinding):RecyclerView.ViewHolder(binding.root){
        val favbtn = binding.imgbtnFav
        fun bind(food: Food, viewModel: FoodViewModel){
            binding.viewmodel = viewModel
            binding.food = food
            if(food.isFav){
                favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
            }else{
                favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
            }
            Picasso.get().load(food.image_url).error(R.drawable.splace_logo).into(binding.imgPhoto)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = getItem(position)
        holder.favbtn.setOnClickListener {
            viewModel.onFavClicked(food)
           notifyItemChanged(position)
        }
        holder.itemView.setOnClickListener {
            listnere.onItemClicked(food)

        }
        holder.bind(food,viewModel)
    }

    companion object{
        private val DiffCallBack = object: DiffUtil.ItemCallback<Food>(){
            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem.id ==newItem.id
            }
            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface IHome{
    fun onItemClicked(food: Food)
}
