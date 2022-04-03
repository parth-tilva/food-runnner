package com.example.module_3_assignment.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.R
import com.example.module_3_assignment.application.foodApplication
import com.example.module_3_assignment.databinding.FragmentHomeBinding
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.model.FoodEntity
import com.example.module_3_assignment.ui.menuOfCanteen.MenuViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.*


val TAG = "home"
class HomeFragment : Fragment(), IHome {

    lateinit var binding: FragmentHomeBinding
    private val viewModel:FoodViewModel by activityViewModels {
        FoodViewModelFactory(
            (activity?.applicationContext as foodApplication)
        )
    }
    private val menuViewModel: MenuViewModel by activityViewModels()

    var checkedItem = 0 // check sort comparator
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FoodAdapter
    private val costLtoH = Comparator<Food> { food1, food2 ->
        val comp =  food1.cost_for_one.compareTo(food2.cost_for_one)
        if(comp == 0){
            food1.name.compareTo(food2.name,true)
        }else{
            comp
        }
    }
    private val costHtoL= Comparator<Food> { food1, food2 ->
        val comp =  -1 * food1.cost_for_one.compareTo(food2.cost_for_one)
        if(comp == 0){
            food1.name.compareTo(food2.name,true)
        }else{
            comp
        }
    }
    private val ratingComparator = Comparator<Food> { food1, food2 ->
        val comp =  -1 * food1.rating.compareTo(food2.rating)
        if(comp == 0){
            food1.name.compareTo(food2.name,true)
        }else{
            comp
        }
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

        requireActivity().supportFragmentManager


        setHasOptionsMenu(true)
        setUpAdapter()
        if(viewModel.foods.value.isNullOrEmpty()){
            getListOfFoodFromApi()
        }
    }

    suspend fun test(id:Int): Boolean{
        val f: FoodEntity? = viewModel.getFoodById(id)
        return f != null

    }


    fun setupAlertSortDialog(){
        val listItems = arrayOf("Cost(Low to High)", "Cost(High to Low)", "Rating")
        val alterSort =  AlertDialog.Builder(requireContext())
            alterSort.apply {
                setTitle("sort by")
                setIcon(R.drawable.custom_splash_theme)
                setSingleChoiceItems(listItems,checkedItem,
                DialogInterface.OnClickListener { dialog, which ->
                    checkedItem = which
                   // dialog.dismiss()
                })
                setPositiveButton("Ok"){ _, _ ->
                    //Toast.makeText(requireContext(),"checked: $checkedItem",Toast.LENGTH_SHORT).show()
                    val list = viewModel.foods.value
                    if(list?.isEmpty() == false){
                        var comparator = costLtoH
                        when(checkedItem){
                            0 -> comparator = costLtoH
                            1 -> comparator = costHtoL
                            2 -> comparator = ratingComparator
                        }
                        Collections.sort(list,comparator)
                        viewModel.setFoods(list)
                        lifecycleScope.launch {
                            delay(100)
                            recyclerView.scrollToPosition(0)
                        }

                    }
                }
                setNegativeButton("Cancel"){ _,_ ->
                }
                    create()
                show()
            }

    }

    private fun setUpAdapter(){
        recyclerView = binding.rvListOfFood
        if(activity!=null){
            adapter = FoodAdapter(viewModel,this)
            recyclerView.adapter = adapter
            viewModel.foods.observe(this.viewLifecycleOwner) {
                adapter.submitList(it.toMutableList())
            }
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
                                val name = jsonObj.getString("name")
                                val rating = jsonObj.getString("rating").toFloat()
                                val  cost = jsonObj.getString("cost_for_one").toInt()
                                val imgUrl = jsonObj.getString("image_url")
                                val isFav = test(id)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.btn_filter){
            setupAlertSortDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_icon,menu)
    }

    override fun onItemClicked(food: Food) {
        menuViewModel.setFoods(listOf())
        val action = HomeFragmentDirections.actionHomeFragmentToMenuFragment(food.id.toString(),food.name)
        findNavController().navigate(action)
    }
}