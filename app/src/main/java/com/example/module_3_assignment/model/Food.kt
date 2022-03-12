package com.example.module_3_assignment.model

data class Food(
    val id: Int,
    val name: String,
    var isFav: Boolean = false,
    val rating: Float,
    val cost_for_one:Int,
    val image_url: String
)

//"id": "1",
//"name": "Pind Tadka",
//"rating": "4.1",
//"cost_for_one": "280",
//"image_url":