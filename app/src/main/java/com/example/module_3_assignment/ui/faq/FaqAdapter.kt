package com.example.module_3_assignment.ui.faq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.module_3_assignment.databinding.QesAnsItemBinding
import com.example.module_3_assignment.model.Faq

class FaqAdapter(val listOfQ:List<Faq>): RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    class ViewHolder(val binding: QesAnsItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(faq: Faq,position: Int){
            binding.txtTitle.text = "Q.${position+1} ${faq.title}"
            binding.txtQue.text = faq.content
        }
    }



    override fun getItemCount(): Int {
        return listOfQ.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfQ[position]
        holder.bind(item,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(QesAnsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
}