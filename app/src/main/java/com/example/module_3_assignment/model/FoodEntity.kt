package com.example.module_3_assignment.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "rating") val rating: Float,
    @ColumnInfo(name = "cost_for_one") val cost_for_one:Int,
    @ColumnInfo(name = "image_url") val image_url: String
)