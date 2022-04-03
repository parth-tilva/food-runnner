package com.example.module_3_assignment.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.ActivityForgotPasswordBinding
import com.example.module_3_assignment.databinding.ActivityResetPasswordBinding
import com.example.module_3_assignment.ui.home.HomeFragment
import com.example.module_3_assignment.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {
    val TAG = "reset"
    lateinit var binding: ActivityResetPasswordBinding
    var mobileNo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        if(intent != null){
            mobileNo = intent.getStringExtra("mobile")
        }
    }



    fun setupUi(){
        val nextBtn = binding.btnSubmit


        nextBtn.setOnClickListener {
            val otp = binding.etOtp.text.toString()
            val newPass = binding.etPassword.text.toString()
            val conformPass = binding.etConfirmPassword.text.toString()
            if(newPass != conformPass){
                Toast.makeText(this,"conform pass not same", Toast.LENGTH_LONG).show()
            }else{
                check(newPass,otp)
            }
        }
    }

    private fun volleyrespond(status:Boolean){
                if(status){
                    val intent = Intent(this@ResetPasswordActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@ResetPasswordActivity,"some error occur", Toast.LENGTH_LONG).show()
                }
    }
    private fun check(pass:String, otp:String):Boolean{
        var flag= false
        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",mobileNo)
        jsonParams.put("password",pass)
        jsonParams.put("otp",otp)
        Log.d(TAG,"mmobile no: $mobileNo, pass: $pass,  otp:$otp")

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"
        val jsonapiRequest = object: JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            {
                if(true){ // check internet
                    try {
                        val jsonObject = it.getJSONObject("data")
                        val success = jsonObject.getBoolean("success")
                        Log.d("reset","success: $success, $jsonObject")
                        flag = success
                        if(success){
                            val successMessage = jsonObject.getString("successMessage")
                            Toast.makeText(this,"apiM: $successMessage", Toast.LENGTH_LONG).show()
                        }else{
                            val errorMessage = jsonObject.getString("errorMessage")
                            Toast.makeText(this,"apiE: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                        volleyrespond(success)
                    }catch (e:Exception){
                        Toast.makeText(this,"error catch e:$e", Toast.LENGTH_LONG).show()
                    }
                }
            },
            {
                Toast.makeText(this,"error volley e:$it", Toast.LENGTH_LONG).show()
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