package com.example.module_3_assignment.model

data class History(val orderId:String,
val resName:String,
val totalCost:String,
val time:String,
val foodItems:MutableList<MenuItem>)
