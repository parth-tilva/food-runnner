package com.example.module_3_assignment.ui.orderhistory

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.FragmentOrderHistroyBinding
import com.example.module_3_assignment.model.History
import com.example.module_3_assignment.model.MenuItem


class OrderHistoryFragment : Fragment() {

    lateinit var binding: FragmentOrderHistroyBinding
    lateinit var adapter: HistoryAdapter
    val TAG = "History"
    var uid=""
    var historyList = mutableListOf<History>()
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderHistroyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        uid = sharedPreferences.getString("id","0")!!
        getData()
    }

    fun setUpUi(){
        adapter = HistoryAdapter()
        binding.rvHistory.adapter = adapter
        adapter.submitList(historyList)
    }

    private fun getData(){
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://13.235.250.119/v2/orders/fetch_result/$uid"

        val jsonRequest = object: JsonObjectRequest(
            Method.GET,
            url,
            null,
            {
                try {
                    val dataObj = it.getJSONObject("data")
                    val success = dataObj.getBoolean("success")
                    if(success){
                        Log.d(TAG,"success: true dataobj: $dataObj")
                        val dataArray = dataObj.getJSONArray("data")
                        for(i in 0 until dataArray.length()){ // orders
                            val currentDataObj = dataArray.getJSONObject(i)
                            val orderId = currentDataObj.getString("order_id")
                            val restaurantName = currentDataObj.getString("restaurant_name")
                            val totalCost = currentDataObj.getString("total_cost")
                            val orderTime = currentDataObj.getString("order_placed_at")
                            val foodItems = currentDataObj.getJSONArray("food_items")
                            val foodList = mutableListOf<MenuItem>()
                            for(i in 0 until foodItems.length()){ // foods
                                val currentFood = foodItems.getJSONObject(i)
                                val  id = currentFood.getString("food_item_id")
                                val name = currentFood.getString("name")
                                val cost = currentFood.getString("cost")
                                val food = MenuItem(id,name,cost,"")
                                foodList.add(food)
                            }
                            val history = History(orderId,restaurantName,totalCost,orderTime,foodList)
                            historyList.add(history)
                        }
                        setUpUi()
                    }else{
                        Log.d(TAG,"success: false dataobj: $dataObj")
                    }
                }catch (e:Exception){
                    Log.d(TAG,"error: $e")
                }
            },
            {
                Log.d(TAG,"volley error: $it")
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "f56132fd80ca31"
                return headers
            }
        }
        queue.add(jsonRequest)
    }
}