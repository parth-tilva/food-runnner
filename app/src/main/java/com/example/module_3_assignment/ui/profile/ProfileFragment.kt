package com.example.module_3_assignment.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity!=null)
        bind()

    }

    fun bind(){

        val name = sharedPreferences.getString("Name","")
        val email = sharedPreferences.getString("Email","")
        val mobile = sharedPreferences.getString("MobileNo","")
        val address = sharedPreferences.getString("address","")
        val password = sharedPreferences.getString("Password","")

        binding.txtName.text = "Name:\t\t$name"
            binding.txtMoblie.text = "Moblie:\t\t\t +91 $mobile"
            binding.txtEmail.text = "Email:\t\t $email"
            binding.txtGender.text = "Address:\t\t $address"
            binding.txtDateOFBirth.text = "Password:\t\t $password"
    }


}