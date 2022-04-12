package com.example.module_3_assignment.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.toolbox.Volley
import com.example.module_3_assignment.database.FoodDataBase
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.model.FoodEntity
import com.example.module_3_assignment.repository.FoodRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodViewModel( appContext: Application): ViewModel() {

    private val repository: FoodRepo
    val allFood: LiveData<List<FoodEntity>>

    private val _foods  = MutableLiveData<List<Food>>()
    var foods: LiveData<List<Food>> = _foods


    fun setFoods(list: List<Food>){
        _foods.value = list
    }

    private fun updateLike(food: Food){
        val list = _foods.value
        if(list != null && list.isNotEmpty()){
            val pos = list.indexOf(food)
            val apply = !list[pos].isFav
            list[pos].apply { isFav=apply }
            _foods.value = list
        }
    }

    init {
        val foodDao = FoodDataBase.getDatabase(appContext).foodDao()
         repository = FoodRepo(foodDao)
        allFood = repository.allFoods
    }

    fun addFoodToList(food:Food){
        if(_foods.value==null){
            _foods.value = listOf(food)
        }else{
            _foods.value = _foods.value!!.plus(food)
        }
    }


    private fun insertFood(foodEntity: FoodEntity ) = viewModelScope.launch {
        repository.insertFood(foodEntity)
    }

     private fun deleteFood(foodEntity: FoodEntity) = viewModelScope.launch {
        repository.deleteFood(foodEntity)
    }


    suspend fun getFoodById(id:Int) = repository.getFoodById(id)


    fun onFavClicked(food: Food){
        val entity = FoodEntity(food.id,food.name,food.rating,food.cost_for_one,food.image_url)
        if(food.isFav){
            deleteFood(entity)
            updateLike(food)
        }else{
            insertFood(entity)
            updateLike(food)
        }
    }
}




// when parameter is there we use factory
class FoodViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            return FoodViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
