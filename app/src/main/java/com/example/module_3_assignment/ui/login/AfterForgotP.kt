package com.example.module_3_assignment.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.module_3_assignment.R

class AfterForgotP : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_forgot_p)
        if(intent != null){
            val mobile = intent.getStringExtra("mobile")
            val email = intent.getStringExtra("email")
            val txt = findViewById<TextView>(R.id.txt)
            txt.text = "mobile : " +mobile +"\n"+"email: "+email
        }
    }
}