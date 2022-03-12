package com.example.module_3_assignment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.module_3_assignment.model.FoodEntity


@Database(entities = [FoodEntity::class], version = 1, exportSchema = false)
abstract class FoodDataBase: RoomDatabase() {

    abstract fun foodDao(): FoodDao

    companion object{
        private var INSTANCE: FoodDataBase? = null

        fun getDatabase(context: Context): FoodDataBase {
            //if the INSTANCE is Not null, then return it,
            //if it is, then create the database
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodDataBase::class.java,
                    "Food-db"
                ).build()
                INSTANCE = instance
                instance
            }

        }

    }

}