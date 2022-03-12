package com.example.module_3_assignment.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.lifecycle.lifecycleScope
import com.example.module_3_assignment.R
import com.example.module_3_assignment.ui.profile.ProfileFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMoblie: EditText
    lateinit var etAddress: EditText
    lateinit var etPassword: EditText
    lateinit var btnRegister: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmial)
        etMoblie = findViewById(R.id.etmoblieNo)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobile = etMoblie.text.toString()
            val address = etAddress.text.toString()
            val password = etPassword.text.toString()

            sharedPreferences.edit().putBoolean("IsLoggedIN",true).apply()
            sharedPreferences.edit().putBoolean("IsSignedIN",true).apply()
            sharedPreferences.edit().putString("Name",name).apply()
            sharedPreferences.edit().putString("Email",email).apply()
            sharedPreferences.edit().putString("MobileNo",mobile).apply()
            sharedPreferences.edit().putString("address",address).apply()
            sharedPreferences.edit().putString("Password",password).apply()

            if(name!="" && mobile!="" && password!=""){
                Toast.makeText(this,"register successfully enter registered credentials",Toast.LENGTH_LONG).show()

                lifecycleScope.launch {
                    delay(1000)
                    withContext(Dispatchers.Main){
                        val intent = Intent(this@RegisterActivity, LogInActivity::class.java)
                        startActivity(intent)
                    }
                }
            }else{
                Toast.makeText(this,"empty credentials",Toast.LENGTH_LONG).show()
            }


        }
    }

}