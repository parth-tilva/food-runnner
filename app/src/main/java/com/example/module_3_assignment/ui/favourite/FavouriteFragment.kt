package com.example.module_3_assignment.ui.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.module_3_assignment.R
import com.example.module_3_assignment.application.foodApplication
import com.example.module_3_assignment.databinding.FragmentFavouriteBinding
import com.example.module_3_assignment.databinding.FragmentHomeBinding
import com.example.module_3_assignment.databinding.ItemFoodBinding
import com.example.module_3_assignment.ui.home.FoodViewModel
import com.example.module_3_assignment.ui.home.FoodViewModelFactory

class FavouriteFragment : Fragment() {


    lateinit var binding: FragmentFavouriteBinding
    lateinit var adapter: FavoritesAdapter
    private val viewModel: FoodViewModel by activityViewModels {
        FoodViewModelFactory(
            (activity?.applicationContext as foodApplication)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        val recyclerView = binding.rvListOfFood
        adapter = FavoritesAdapter(viewModel)
        viewModel.allFood.observe(this.viewLifecycleOwner,{
            adapter.submitList(it)
        })
        recyclerView.adapter = adapter
    }
}