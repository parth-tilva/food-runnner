package com.example.module_3_assignment.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.module_3_assignment.R

class AfterRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_register)
        if(intent != null){
            val name = intent.getStringExtra("name")
            val email = intent.getStringExtra("email")
            val mobile = intent.getStringExtra("mobile")
            val address = intent.getStringExtra("address")
            val password = intent.getStringExtra("password")

            val txt = findViewById<TextView>(R.id.txt)
            txt.text = "name: $name \n emial: $email \n mobile: $mobile \n address: $address \n password: $password"
        }
    }
}

