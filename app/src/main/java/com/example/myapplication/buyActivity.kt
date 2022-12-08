package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide.init
import com.example.myapplication.adapter.MyProductAdapter
import com.example.myapplication.eventBus.UpdateCartEvent
import com.example.myapplication.listener.ICartLoadListener
import com.example.myapplication.listener.IProductLoadListener
import com.example.myapplication.model.CartModel
import com.example.myapplication.model.product
import com.example.myapplication.utils.SpaceItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.buy_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class buyActivity: AppCompatActivity(), IProductLoadListener, ICartLoadListener {
    private lateinit var backButton: Button
    lateinit var productListener: IProductLoadListener
    lateinit var cartLoadListener: ICartLoadListener

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onUpdateCartEvent(event: UpdateCartEvent){
        countCartFromFireBase()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.buy_activity)
        init()
        loadProductFromFirebase()
        countCartFromFireBase()

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun countCartFromFireBase() {
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }

            })
    }

    private fun loadProductFromFirebase(){
        val products: MutableList<product> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Drink")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (productSnapshot in snapshot.children){
                            val productModel = productSnapshot.getValue(product::class.java)
                            productModel!!.key = productSnapshot.key
                            products.add(productModel)
                        }
                        productListener.onProductLoadSuccess(products)
                    }
                    else{
                        productListener.onProductLoadFailed("Error")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    productListener.onProductLoadFailed(error.message)
                }
            })
    }

    private fun init(){
        productListener = this
        cartLoadListener = this

        val gridLayoutManager = GridLayoutManager(this, 2)
        recycler_product.layoutManager = gridLayoutManager
        recycler_product.addItemDecoration(SpaceItemDecoration())

        cart.setOnClickListener{ startActivity(Intent(this,cartActivity::class.java))}

    }

    override fun onProductLoadSuccess(Products: List<product>?) {
        val adapter = MyProductAdapter(this, Products!!, cartLoadListener)
        recycler_product.adapter = adapter
    }

    override fun onProductLoadFailed(message: String?) {
        Snackbar.make(mainlayout,message!!,Snackbar.LENGTH_LONG).show()
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for (cartModel in cartModelList!!) cartSum += cartModel!!.quantityOfBuy
        badge!!.setNumber(cartSum)
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainlayout,message!!,Snackbar.LENGTH_LONG).show()
    }
}