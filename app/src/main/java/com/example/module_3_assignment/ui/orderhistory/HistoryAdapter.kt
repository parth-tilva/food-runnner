package com.example.module_3_assignment.ui.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_3_assignment.databinding.HistoryItemBinding
import com.example.module_3_assignment.model.History
import com.example.module_3_assignment.ui.mycart.CartAdapter

class HistoryAdapter():ListAdapter<History,HistoryAdapter.HistoryViewHolder>(DiffCallbacks) {
    class HistoryViewHolder(val binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(history: History){
            binding.txtTime.text = history.time
            binding.txtInstruction.text = history.resName
            val adapter = CartAdapter()
            binding.rvMenuItems.adapter = adapter
            adapter.submitList(history.foodItems)
        }
    }

    companion object{
        val DiffCallbacks = object:DiffUtil.ItemCallback<History>(){
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.orderId == newItem.orderId
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }
}

