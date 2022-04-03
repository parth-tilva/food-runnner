package com.example.module_3_assignment.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.R
import com.example.module_3_assignment.model.User
import com.example.module_3_assignment.ui.home.HomeFragment
import com.example.module_3_assignment.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var etMoblie :EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    private var mobile = ""
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

//        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
//        val isLoggedIn = sharedPreferences.getBoolean("IsLoggedIN",false)
//
//        if(!isLoggedIn){
//            Toast.makeText(this,"Register first not register user!!", Toast.LENGTH_LONG).show()
//            val intent = Intent(this@ForgotPasswordActivity, RegisterActivity::class.java)
//            startActivity(intent)
//        }

        etMoblie = findViewById(R.id.etmoblieNo)
        etEmail = findViewById(R.id.etEmial)
        btnNext  = findViewById(R.id.btnNext)


        btnNext.setOnClickListener {
            mobile = etMoblie.text.toString()
            val email = etEmail.text.toString()
            checkIsSame(mobile,email)

        }
    }

    private fun gotoNext(mobile:String) {
        lifecycleScope.launch {
            delay(100)
            withContext(Dispatchers.Main){
                val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                intent.putExtra("mobile",mobile)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun volleyRespond(status:Boolean){
        if(status){
            gotoNext(mobile)
        } else{
            Toast.makeText(this@ForgotPasswordActivity,"some error occur",Toast.LENGTH_LONG).show()
        }
    }

    private  fun checkIsSame(mobileNo: String, email: String):Boolean {
//        val mobi = sharedPreferences.getString("MobileNo","")
//        val em = sharedPreferences.getString("Email","")
//        if(mobileNo == mobi &&  email == em)
//            return true



        var flag= false
        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",mobileNo)
        jsonParams.put("email",email)

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
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
                            val firstTry = jsonObject.getBoolean("first_try")
                            if(firstTry){
                                Toast.makeText(this,"password is sent to email",Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this,"already password is sent to email (can send 1 time in 24hours)",Toast.LENGTH_LONG).show()
                            }
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


}