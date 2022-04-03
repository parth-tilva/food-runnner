package com.example.module_3_assignment.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.R
import com.example.module_3_assignment.model.User
import com.example.module_3_assignment.ui.main.MainActivity
import org.json.JSONObject

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
        }else if(!isLoggedIn && isSignedIn){
            Toast.makeText(this,"Enter mobile and password",Toast.LENGTH_LONG).show()
        }else if(!isLoggedIn && !isSignedIn){
            Toast.makeText(this,"Register first",Toast.LENGTH_LONG).show()
//            val intent = Intent(this@LogInActivity, RegisterActivity::class.java)
//            startActivity(intent)
        }else if(!isSignedIn && isLoggedIn){
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
            if(CheckAllFields()){
                checkIsSame(mobileNo,password)
            }
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LogInActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        txtSignUp.setOnClickListener {
            val intent = Intent(this@LogInActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun CheckAllFields(): Boolean {
        if (etMoblie.length() == 0) {
            etMoblie.error = "This field is required"
            return false
        }
        if (etPassword.length() === 0) {
            etPassword.setError("This field is required")
            return false
        }
        if (etMoblie.length() < 10) {
            etMoblie.setError("Mobile number must be 10 digits")
            return false
        }
        if (etPassword.length() < 6) {
            etPassword.error = "Password must be minimum 6 characters"
            return false
        }
        return true
    }
    fun volleyRespond(status:Boolean){
                if(status){
                    sharedPreferences.edit().putBoolean("IsLoggedIN",true).commit()
                    sharedPreferences.edit().putBoolean("IsSignedIN",true).commit()
                    gotoNext()
                } else{
                    Toast.makeText(this@LogInActivity,"some error occur",Toast.LENGTH_LONG).show()
                }
    }

    private  fun checkIsSame(mobileNo: String, password: String):Boolean {
//        val mobi = sharedPreferences.getString("MobileNo","")
//        val pass = sharedPreferences.getString("Password","")
//        if(mobileNo == mobi &&  password == pass)
//            return true
        var flag= false
        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",mobileNo)
        jsonParams.put("password",password)

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/login/fetch_result"
        val jsonapiRequest = object: JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            {
                if(true){ // check internet
                    try {
                        val jsonObject = it.getJSONObject("data")
                        val success = jsonObject.getBoolean("success")
                        Log.d("register","success: $success")
                        flag = success
                        if(success){
                            val jsonObjectChild = jsonObject.getJSONObject("data")
                            Log.d("register","child obj: $jsonObjectChild")
                            val uid = jsonObjectChild.getString("user_id")
                            val name = jsonObjectChild.getString("name")
                            val email = jsonObjectChild.getString("email")
                            val mobile = jsonObjectChild.getString("mobile_number")
                            val address =     jsonObjectChild.getString("address")
                            val responseUser = User(name,mobile,uid,address,email)
                            sharedPreferences.edit().putBoolean("IsLoggedIN",true).commit()
                            sharedPreferences.edit().putBoolean("IsSignedIN",true).commit()
                            sharedPreferences.edit().putString("id",uid).commit()
                            sharedPreferences.edit().putString("Name",name).commit()
                            sharedPreferences.edit().putString("Email",email).commit()
                            sharedPreferences.edit().putString("MobileNo",mobile).commit()
                            sharedPreferences.edit().putString("address",address).commit()
                            sharedPreferences.edit().putString("Password",uid).commit()
                            Toast.makeText(this,"successfully logged in uid: $uid, name: $name",Toast.LENGTH_LONG).show()
                        }else{
                            val errorMessage = jsonObject.getString("errorMessage")
                            Toast.makeText(this,"apiE: $errorMessage",Toast.LENGTH_LONG).show()
                        }
                        volleyRespond(success)
                    }catch (e:Exception){
                        Toast.makeText(this,"error catch e:$e",Toast.LENGTH_LONG).show()
                    }
                }
            },
            {
                Toast.makeText(this,"error volley e:$it",Toast.LENGTH_LONG).show()
            }

        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-type"]="application/json"
                headers["token"] = "f56132fd80ca31"
                return headers
            }
        }
        queue.add(jsonapiRequest)
        return flag
    }

    private fun gotoNext() {
        val intent = Intent(this@LogInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}