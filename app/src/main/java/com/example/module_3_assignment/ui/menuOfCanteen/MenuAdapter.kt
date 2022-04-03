package com.example.module_3_assignment.ui.menuOfCanteen

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.MenuItemBinding
import com.example.module_3_assignment.model.MenuItem

class MenuAdapter(val viewModel: MenuViewModel,val listner: IMenu,val context: Context):ListAdapter<MenuItem,MenuAdapter.MenuViewHolder>(DiffCallBack) {
    class MenuViewHolder(val binding: MenuItemBinding): RecyclerView.ViewHolder(binding.root){
        val btnAdd = binding.btnAdd
        fun bind(menuItem: MenuItem,viewModel: MenuViewModel,context: Context){
            binding.apply {
                txtIndex.text = adapterPosition.toString()
                txtFoodName.text = menuItem.name
                txtPrice.text = "RS."+menuItem.price

            }
        }
    }

    companion object {
        private val DiffCallBack = object :DiffUtil.ItemCallback<MenuItem>(){
            override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,viewModel,context)
        if(viewModel.isOrdered(item)){
            holder.btnAdd.text = "remove"
            holder.btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
        }else{
            holder.btnAdd.text = "add"
            holder.btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
        }
        holder.btnAdd.setOnClickListener {
            holder.apply {
                if(!viewModel.isOrdered(item)){  // add
                    btnAdd.text = "remove"
                    btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
                    listner.onItemClickedAdd(item)
                }else{   // remove
                    btnAdd.text = "add"
                    btnAdd.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                    listner.onItemRemove(item)
                }
            }
        }
    }
}

interface IMenu{
    fun onItemClickedAdd(menuItem: MenuItem)
    fun onItemRemove(menuItem:MenuItem)
}