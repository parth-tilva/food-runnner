package com.example.module_3_assignment.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.ItemFoodBinding
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.model.FoodEntity
import com.example.module_3_assignment.ui.home.FoodAdapter
import com.example.module_3_assignment.ui.home.FoodViewModel
import com.example.module_3_assignment.ui.home.HomeFragmentDirections
import com.squareup.picasso.Picasso

class FavoritesAdapter(private val viewModel: FoodViewModel,val listner: IFav): ListAdapter<FoodEntity,FavoritesAdapter.FavoriteViewHolder>(
    DiffCallBack) {

    companion object{
        private val DiffCallBack = object: DiffUtil.ItemCallback<FoodEntity>(){
            override fun areItemsTheSame(oldItem: FoodEntity, newItem: FoodEntity): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: FoodEntity, newItem: FoodEntity): Boolean {
                return (oldItem.id == oldItem.id)
            }
        }
    }

    class FavoriteViewHolder(val binding: ItemFoodBinding): RecyclerView.ViewHolder(binding.root){
        val favBtn = binding.imgbtnFav

        fun bind(foodEntity: FoodEntity,viewModel: FoodViewModel){
            val food = Food(foodEntity.id,foodEntity.name,false,foodEntity.rating,foodEntity.cost_for_one,foodEntity.image_url)
            binding.apply {
                this.food = food
                viewmodel = viewmodel
                Picasso.get().load(food.image_url).error(R.drawable.splace_logo).into(imgPhoto)
            }

            favBtn.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val entity = getItem(position)
        holder.bind(entity,viewModel)
        holder.favBtn.setOnClickListener {
            viewModel.onFavClicked(
                Food(id = entity.id,
                    name = entity.name,
                isFav = true,
                rating = entity.rating,
                cost_for_one = entity.cost_for_one,
                image_url = entity.image_url)
            )
        }
        holder.itemView.setOnClickListener {
            listner.onItemClicked(
                Food(id = entity.id,
                    name = entity.name,
                    isFav = true,
                    rating = entity.rating,
                    cost_for_one = entity.cost_for_one,
                    image_url = entity.image_url)
            )
        }
    }
}

interface IFav{
    fun onItemClicked(food: Food)
}