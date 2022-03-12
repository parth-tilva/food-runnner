package com.example.module_3_assignment.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.module_3_assignment.R
import com.example.module_3_assignment.ui.home.HomeFragment
import com.example.module_3_assignment.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var etMoblie :EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("IsLoggedIN",false)

        if(!isLoggedIn){
            Toast.makeText(this,"Register first not register user!!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@ForgotPasswordActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        etMoblie = findViewById(R.id.etmoblieNo)
        etEmail = findViewById(R.id.etEmial)
        btnNext  = findViewById(R.id.btnNext)


        btnNext.setOnClickListener {
            val mobile = etMoblie.text.toString()
            val email = etEmail.text.toString()

            if(checkIsSame(mobile,email)){
                gotoNext()
            } else{
                Toast.makeText(this,"wrong email and password",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun gotoNext() {

        lifecycleScope.launch {
            delay(2000)
            withContext(Dispatchers.Main){
                val intent = Intent(this@ForgotPasswordActivity, HomeFragment::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun checkIsSame(mobileNo: String, email: String):Boolean {
        val mobi = sharedPreferences.getString("MobileNo","")
        val em = sharedPreferences.getString("Email","")
        if(mobileNo == mobi &&  email == em)
            return true
        return false
    }



}