package com.example.myapplication.listener

import android.view.View
import java.text.FieldPosition

interface IRecyclerClickListener {
    fun onItemClickListener(view:View?, position:Int)
}