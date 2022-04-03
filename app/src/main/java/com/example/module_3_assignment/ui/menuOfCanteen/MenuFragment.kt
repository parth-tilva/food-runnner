package com.example.module_3_assignment.ui.menuOfCanteen

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.FragmentMenuBinding
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.model.MenuItem

class MenuFragment : Fragment(), IMenu {

    lateinit var binding: FragmentMenuBinding
    lateinit var adapter: MenuAdapter
    val TAG = "Menu"
    var menuList  = mutableListOf<MenuItem>()
    var orderList  = mutableListOf<MenuItem>()
    val viewModel: MenuViewModel by activityViewModels()
    val navigationArgs: MenuFragmentArgs by navArgs()
    private var resId = ""
    private var resName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resId = navigationArgs.restaurantId
        resName = navigationArgs.resName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(menuList.size<=0){
            getData()
        }else{
            setupUi()
        }
    }


    fun setupUi(){
        adapter = MenuAdapter(viewModel,this,requireContext())
        binding.rvMenuItems.adapter = adapter
        adapter.submitList(menuList)

        binding.btnProceedToCart.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToMyCartFragment(resId,resName)
            findNavController().navigate(action)
        }

        viewModel.order.observe(viewLifecycleOwner) {
            Log.d(TAG,"ordersuze observe ${it.size}")
            if(it.size>0){
                binding.btnProceedToCart.visibility = View.VISIBLE
            }else{
                binding.btnProceedToCart.visibility = View.GONE
            }
        }
    }



    fun getData(){
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"

        val jsonRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            {
                try {
                    val jsonObj = it.getJSONObject("data")
                    val success = jsonObj.getBoolean("success")
                    if(success){
                        Log.d(TAG,"success: true : $jsonObj")
                        val dataArray = jsonObj.getJSONArray("data")

                        for (i in 0 until dataArray.length()){
                           val currData =  dataArray.getJSONObject(i)
                            val id = currData.getString("id")
                            val name = currData.getString("name")
                            val price = currData.getString("cost_for_one")
                            val resId = currData.getString("restaurant_id")
                            val menuItem = MenuItem(id,name,price,resId)
                            menuList.add(menuItem)
                        }
                        setupUi()
                    }else{
                        Log.d(TAG,"success: false : $jsonObj")
                    }
                }catch (e:Exception){
                    Log.d(TAG,"ex: $e")
                }
            },
            {
                Log.d("Home","json Exception $it")
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers   = HashMap<String,String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "f56132fd80ca31"
                return headers
            }
        }
        queue.add(jsonRequest)
    }


    override fun onItemClickedAdd(menuItem: MenuItem) {
        orderList.add(menuItem)
        viewModel.setFoods(orderList)
    }

    override fun onItemRemove(menuItem: MenuItem) {
        orderList.remove(menuItem)
        viewModel.setFoods(orderList)
        Log.d(TAG,"orderlist from viewmodel $orderList, ${orderList.size}")
    }
}