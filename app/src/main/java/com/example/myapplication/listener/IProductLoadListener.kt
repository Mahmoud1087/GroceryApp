package com.example.myapplication.listener

import com.example.myapplication.model.product

interface IProductLoadListener {

    fun onProductLoadSuccess(Products:List<product>?)
    fun onProductLoadFailed(message:String?)
}