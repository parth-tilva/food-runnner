package com.example.module_3_assignment.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.module_3_assignment.R

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var etMoblie :EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        etMoblie = findViewById(R.id.etmoblieNo)
        etEmail = findViewById(R.id.etEmial)
        btnNext  = findViewById(R.id.btnNext)


        btnNext.setOnClickListener {
            val mobile = etMoblie.text.toString()
            val email = etEmail.text.toString()
            val intent = Intent(this@ForgotPasswordActivity, AfterForgotP::class.java)
            intent.putExtra("mobile",mobile)
            intent.putExtra("email",email)
            startActivity(intent)
        }

    }
}