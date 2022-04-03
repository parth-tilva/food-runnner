package com.example.module_3_assignment.ui.conform

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.module_3_assignment.databinding.FragmentComformationBinding


class ConfirmationFragment : Fragment() {
    lateinit var binding: FragmentComformationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComformationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOk.setOnClickListener {
            val action = ConfirmationFragmentDirections.actionConfirmationFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}