package com.example.module_3_assignment.ui.mycart

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.R
import com.example.module_3_assignment.databinding.FragmentMyCartBinding
import com.example.module_3_assignment.model.MenuItem
import com.example.module_3_assignment.ui.menuOfCanteen.MenuAdapter
import com.example.module_3_assignment.ui.menuOfCanteen.MenuViewModel
import org.json.JSONArray
import org.json.JSONObject

class MyCartFragment : Fragment() {
    lateinit var binding: FragmentMyCartBinding
    lateinit var adapter: CartAdapter
    val TAG = "cart"
    private var resId = ""
    private var resName = ""
    private var total = 0
    private var uid = "0"
    private val navigationArgs: MyCartFragmentArgs by navArgs()
    val viewModel: MenuViewModel by activityViewModels()
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyCartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resId = navigationArgs.resId
        resName = navigationArgs.resName
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        uid = sharedPreferences.getString("id","0")!!
        setupId()
    }
    private fun setupId(){    // todo: local database??
        adapter = CartAdapter()
        binding.rvMenuItems.adapter = adapter
        binding.txtInstruction.text = "Ordering From: "+resName
        binding.btnProceedToCart.visibility = View.VISIBLE
        viewModel.order.observe(viewLifecycleOwner) {
            Log.d(TAG,"ordersuze observe ${it.size}")
            total = viewModel.getTotal()
            Log.d(TAG,"total $total")
            adapter.submitList(it)
            binding.btnProceedToCart.text = "Place Order(Total:Rs. $total)"
        }
        binding.btnProceedToCart.setOnClickListener {
            sendToApi()
        }
    }
    fun sendToApi(){
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        val jsonParams = JSONObject()
        jsonParams.put("user_id",uid)
        jsonParams.put("restaurant_id",resId)
        jsonParams.put("total_cost",total)

        val jsonFoodArray = JSONArray()
        val list = viewModel.order.value!!
        for(i in list.indices){
            val temp = JSONObject()
            temp.put("Food_item_id",list[i].id)
            jsonFoodArray.put(temp)
        }
        jsonParams.put("food",jsonFoodArray)
        Log.d(TAG,"jsonparam: $jsonParams")


        val jsonObjectRequest =  object:JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            {
                try{
                    val dataObj = it.getJSONObject("data")
                    val success = dataObj.getBoolean("success")
                    if(success){
                        Log.d(TAG," success: true : $dataObj")
                        val action = MyCartFragmentDirections.actionMyCartFragmentToConfirmationFragment()
                        findNavController().navigate(action)
                    }else{
                        Log.d(TAG," success: false : $dataObj")
                    }
                }catch (e:Exception){
                    Log.d(TAG," error: $e")
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
        queue.add(jsonObjectRequest)
    }


}