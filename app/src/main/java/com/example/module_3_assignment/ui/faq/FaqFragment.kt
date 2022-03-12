package com.example.module_3_assignment.ui.faq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.FragmentFaqBinding
import com.example.module_3_assignment.model.Faq


class FaqFragment : Fragment() {

    lateinit var binding: FragmentFaqBinding
     private var listFaqs = mutableListOf<Faq>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFaqBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listFaqs.add(Faq("how to implement like button","Hello i don't know how to implement like button properly and how to update recycler view but i have implemented i think poorly"))
        listFaqs.add(Faq("how to implement like button","Hello i don't know how to implement like button properly and how to update recycler view but i have implemented i think poorly"))
        listFaqs.add(Faq("how to implement like button","Hello i don't know how to implement like button properly and how to update recycler view but i have implemented i think poorly"))
        listFaqs.add(Faq("how to implement like button","Hello i don't know how to implement like button properly and how to update recycler view but i have implemented i think poorly"))
        listFaqs.add(Faq("doubt in async vs coroutine","how to use coroutine in bookhub app insted of async task"))
        binding.rvListOfFood.adapter = FaqAdapter(listFaqs)
    }

}