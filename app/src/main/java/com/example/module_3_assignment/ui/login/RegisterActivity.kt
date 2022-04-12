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
import androidx.annotation.MainThread
import androidx.lifecycle.lifecycleScope
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.R
import com.example.module_3_assignment.model.User
import com.example.module_3_assignment.ui.home.HomeFragment
import com.example.module_3_assignment.ui.main.MainActivity
import com.example.module_3_assignment.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.reflect.Method

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMoblie: EditText
    lateinit var etAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirm: EditText
    lateinit var btnRegister: Button
    lateinit var user: User
    lateinit var sharedPreferences: SharedPreferences
    val TAG = "register"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmial)
        etMoblie = findViewById(R.id.etmoblieNo)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirm = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)


        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobile = etMoblie.text.toString()
            val address = etAddress.text.toString()
            val password = etPassword.text.toString()

            user = User(name,mobile,password,address,email)


            if(CheckAllFields()){
                vollyRequest()
            }else{
                Toast.makeText(this,"empty credentials",Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun CheckAllFields(): Boolean {
        if(etName.length() ==0){
            etName.error = "This field is required"
            return false
        }
        if(etName.length() < 3){
            etName.error = "This field must be 3 character"
            return false
        }
        if (etMoblie.length() == 0) {
            etMoblie.error = "This field is required"
            return false
        }
        if (etMoblie.length() != 10) {
            etMoblie.setError("Mobile number must be 10 digits")
            return false
        }
        if (etConfirm.length() == 0) {
            etConfirm.setError("This field is required")
            return false
        }
        if (etConfirm.length() < 6) {
            etConfirm.error = "Password must be minimum 6 characters"
            return false
        }
        if (etPassword.length() == 0) {
            etPassword.setError("This field is required")
            return false
        }
        if (etPassword.length() < 6) {
            etPassword.error = "Password must be minimum 6 characters"
            return false
        }
        if (etPassword.text.toString() != etConfirm.text.toString()) {
            etConfirm.error = "confirm password is not same"
            return false
        }
        if(etEmail.length() == 0){
            etEmail.error = "This field is required"
            return false
        }
        val email = etEmail.text.toString()
        if(!email.contains("@") || !email.contains(".com")){
            etEmail.error = "Invalid email"
            return false
        }
        return true
    }

    private fun gotoNext(){
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun volleyRespond(status:Boolean){
        if(status){
            Toast.makeText(this@RegisterActivity,"register successfully enter registered credentials",Toast.LENGTH_LONG).show()
            gotoNext()
        }else{
            Toast.makeText(this@RegisterActivity,"some error occur",Toast.LENGTH_LONG).show()
        }
    }


    private  fun vollyRequest():Boolean{
            var flag= false
        val jsonParams = JSONObject()
        jsonParams.put("name",user.name)
        jsonParams.put("mobile_number",user.mobile)
        jsonParams.put("password",user.password)
        jsonParams.put("address",user.address)
        jsonParams.put("email",user.email)

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/register/fetch_result"
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
                            Toast.makeText(this,"successfully register uid: $uid, name: $name",Toast.LENGTH_LONG).show()
                            volleyRespond(success)
                        }else{
                            val errorMessage = jsonObject.getString("errorMessage")
                            Toast.makeText(this,"error: $errorMessage",Toast.LENGTH_LONG).show()
                        }
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

//sharedPreferences.edit().putBoolean("IsLoggedIN",true).commit()
//            sharedPreferences.edit().putBoolean("IsSignedIN",true).commit()
//            sharedPreferences.edit().putString("Name",name).commit()
//            sharedPreferences.edit().putString("Email",email).commit()
//            sharedPreferences.edit().putString("MobileNo",mobile).commit()
//            sharedPreferences.edit().putString("address",address).commit()
//            sharedPreferences.edit().putString("Password",password).commit()
