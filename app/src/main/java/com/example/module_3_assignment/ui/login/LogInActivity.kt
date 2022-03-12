package com.example.module_3_assignment.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
        val isSignedIn = sharedPreferences.getBoolean("IsSignedIN",false)


        if(isLoggedIn && isSignedIn){
            gotoNext()
        }else if(isLoggedIn && !isSignedIn){
            Toast.makeText(this,"Enter mobile and password",Toast.LENGTH_LONG).show()
        }else if(!isLoggedIn && !isSignedIn){
            Toast.makeText(this,"Register first",Toast.LENGTH_LONG).show()
            val intent = Intent(this@LogInActivity, RegisterActivity::class.java)
            startActivity(intent)
        }else if(isSignedIn && !isLoggedIn){
            Toast.makeText(this,"error is sharedpreferences saves",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"something error in logging",Toast.LENGTH_LONG).show()
        }

        etMoblie = findViewById(R.id.etMoblieNo)
        etPassword = findViewById(R.id.etPassword)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)
        btnLogIn = findViewById(R.id.btnLogIn)


        btnLogIn.setOnClickListener {
            val mobileNo = etMoblie.text.toString()
            val password = etPassword.text.toString()
            if(mobileNo == ""||password == ""){
                Toast.makeText(this,"Details not entered!!!",Toast.LENGTH_LONG).show()
            }else if(checkIsSame(mobileNo,password)){
                gotoNext()
            } else{
                Toast.makeText(this,"wrong password try forget password",Toast.LENGTH_LONG).show()
            }
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

    private fun checkIsSame(mobileNo: String, password: String):Boolean {
        val mobi = sharedPreferences.getString("MobileNo","")
        val pass = sharedPreferences.getString("Password","")
        if(mobileNo == mobi &&  password == pass)
            return true
        return false
    }

    private fun gotoNext() {
        val intent = Intent(this@LogInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}