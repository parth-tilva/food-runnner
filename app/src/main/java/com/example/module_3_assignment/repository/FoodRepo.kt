package com.example.module_3_assignment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.module_3_assignment.database.FoodDao
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.model.FoodEntity

class FoodRepo(private val foodDao: FoodDao) {
    val allFoods: LiveData<List<FoodEntity>> = foodDao.getAllFoodItem().asLiveData()

    suspend fun insertFood(foodEntity: FoodEntity){
        foodDao.insertFoodItem(foodEntity)
    }

    suspend fun deleteFood(foodEntity: FoodEntity){
        foodDao.deleteFoodItem(foodEntity)
    }

    suspend fun updateFood(foodEntity: FoodEntity){
        foodDao.updateFoodItem(foodEntity)
    }


    suspend fun getFoodById(id: Int):FoodEntity{
        return foodDao.getFoodById(id)
    }
}