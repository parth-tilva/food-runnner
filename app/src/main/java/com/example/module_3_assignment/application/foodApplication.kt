package com.example.module_3_assignment.application

import android.app.Application
import com.example.module_3_assignment.database.FoodDataBase

class foodApplication: Application() {
    val database:FoodDataBase
        by lazy{FoodDataBase.getDatabase(this)}
}

