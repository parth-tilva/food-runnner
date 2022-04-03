package com.example.module_3_assignment.ui.menuOfCanteen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.module_3_assignment.model.Food
import com.example.module_3_assignment.model.MenuItem


class MenuViewModel(): ViewModel(){

    val TAG = "menuviewmodel"


    private val _order  = MutableLiveData<List<MenuItem>>()
    var order: LiveData<List<MenuItem>> = _order

    fun setFoods(list: List<MenuItem>){
        _order.value = list
    }

    fun isOrdered(menuItem: MenuItem):Boolean{
        if(order.value!=null && order.value!!.size>0){
            Log.d(TAG,"not null or empty")
            val pos = order.value!!.indexOf(menuItem)
            if(pos>=0)
                return true
        }
        return false
    }

    fun getTotal():Int{
        var total = 0
        if(_order.value!=null && _order.value!!.size>0){
            val list = _order.value!!
            for(i in list.indices){
                total += list[i].price.toInt()
            }
        }
        return total
    }
}