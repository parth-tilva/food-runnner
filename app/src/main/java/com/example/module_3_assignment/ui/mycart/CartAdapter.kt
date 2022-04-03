package com.example.module_3_assignment.ui.mycart


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_3_assignment.databinding.CartItemBinding
import com.example.module_3_assignment.model.MenuItem

class CartAdapter():ListAdapter<MenuItem,CartAdapter.CartViewHolder>(DiffCallBack) {
    class CartViewHolder(val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(menuItem: MenuItem){
            binding.apply {
                txtFoodName.text =  menuItem.name
                txtFoodPrice.text = "RS."+menuItem.price
            }
        }
    }

    companion object{
        val DiffCallBack = object : DiffUtil.ItemCallback<MenuItem>(){
            override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}