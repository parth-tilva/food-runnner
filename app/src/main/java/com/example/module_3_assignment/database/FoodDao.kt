package com.example.module_3_assignment.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.model.FoodEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(food: FoodEntity)

    @Delete
    suspend fun deleteFoodItem(food: FoodEntity)

    @Update
    suspend fun updateFoodItem(food: FoodEntity)

    @Query("SELECT * FROM food order by name asc " )
    fun getAllFoodItem(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food  WHERE id = :id")
    suspend fun getFoodById(id: Int): FoodEntity
}