package com.example.module_3_assignment.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.module_3_assignment.R
import com.example.module_3_assignment.ui.main.MainActivity

class LogInActivity : AppCompatActivity() {
    lateinit var etMoblie: EditText
    lateinit var etPassword: EditText
    lateinit var txtForgotPassword: TextView
    lateinit var txtSignUp: TextView
    lateinit var btnLogIn: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Module_3_assignment)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("IsLoggedIN",false)

        if(isLoggedIn){
            gotoNext()
        }
        etMoblie = findViewById(R.id.etMoblieNo)
        etPassword = findViewById(R.id.etPassword)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)
        btnLogIn = findViewById(R.id.btnLogIn)


        btnLogIn.setOnClickListener {
            val mobileNo = etMoblie.text.toString()
            val password = etPassword.text.toString()
            setSharedPreferces(mobileNo,password)
            gotoNext()
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LogInActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        txtSignUp.setOnClickListener {
            val intent = Intent(this@LogInActivity, RegisterActivity::class.java)
            startActivity(intent)
        }


    }

    private fun gotoNext() {
        val intent = Intent(this@LogInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun setSharedPreferces(moblieNo:String, password: String){
        sharedPreferences.edit().putBoolean("IsLoggedIN",true).apply()
        sharedPreferences.edit().putString("MobileNo",moblieNo).apply()
        sharedPreferences.edit().putString("Password",password).apply()
    }
}