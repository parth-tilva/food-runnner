package com.example.module_3_assignment.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.application.foodApplication
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.databinding.FragmentHomeBinding
import com.example.module_3_assignment.model.FoodEntity
import kotlinx.coroutines.launch
import org.json.JSONException

class HomeFragment : Fragment(){

    lateinit var binding: FragmentHomeBinding
    private val viewModel:FoodViewModel by activityViewModels {
        FoodViewModelFactory(
            (activity?.applicationContext as foodApplication)
        )
    }

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FoodAdapter
     var foodList : MutableList<Food> = mutableListOf<Food>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        viewModel.resetFoodEntity()
        if(viewModel.foods.value==null){
            Log.d("home","null right")  // always false
        }
        getListOfFoodFromApi()
    }

    suspend fun test(id:Int): Boolean{
        val f: FoodEntity? = viewModel.getFoodById(id)
        return f != null
    }


    private fun setUpAdapter(){
        recyclerView = binding.rvListOfFood
        if(activity!=null){
            adapter = FoodAdapter(viewModel)
            recyclerView.adapter = adapter
            viewModel.foods.observe(this.viewLifecycleOwner,{
                adapter.submitList(it)
            })
        }
    }

    private fun getListOfFoodFromApi(){
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://13.235.250.119/v2/restaurants/fetch_result"
        val jsonapiRequest = object: JsonObjectRequest(
            Method.GET,
            url,
            null,
            {
                val adjIt = it.getJSONObject("data")
                try{
                    val isSuccess = adjIt.getBoolean("success")
                    if(isSuccess){
                        val data = adjIt.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val jsonObj = data.getJSONObject(i)
                            val id = jsonObj.getString("id").toInt()
                            lifecycleScope.launch {
                                val isFav = test(id)
                                val name = jsonObj.getString("name")
                                val rating = jsonObj.getString("rating").toFloat()
                                val  cost = jsonObj.getString("cost_for_one").toInt()
                                val imgUrl = jsonObj.getString("image_url")
                                val foodObj = Food(
                                    id=id,
                                    name = name,
                                    isFav=isFav,
                                    rating = rating,
                                    cost_for_one =  cost,
                                    image_url = imgUrl)
                                viewModel.addFoodToList(foodObj)
                            }
                        }
                    }else{
                        Log.d("Home","respponse.sucess.false")
                    }
                }catch (e: JSONException){
                    Log.d("Home","json Exception $e")
                }
            },
            {
                Log.d("Home","json Exception $it")
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-Type"]="application/json"
                headers["token"] = "f56132fd80ca31"
                return headers
            }
        }
        queue.add(jsonapiRequest)
    }
}