package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.MyCartAdapter
import com.example.myapplication.databinding.ActivityCartBinding
import com.example.myapplication.eventBus.UpdateCartEvent
import com.example.myapplication.listener.ICartLoadListener
import com.example.myapplication.model.CartModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.buy_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class cartActivity : AppCompatActivity(), ICartLoadListener {

    private lateinit var backButton: Button
    private lateinit var buy: Button
    private lateinit var binding: ActivityCartBinding
    private lateinit var database: DatabaseReference
    var cartLoadListener: ICartLoadListener?= null


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
        loadCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        binding = ActivityCartBinding.inflate(layoutInflater)
        init()
        loadCartFromFirebase()

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, buyActivity::class.java)
            startActivity(intent)
        }

        //buy = findViewById(R.id.buy)
        binding.buy.setOnClickListener{
            database = FirebaseDatabase.getInstance().getReference("Cart")
            database.child("UNIQUE_USER_ID").removeValue().addOnSuccessListener {
                Toast.makeText(this, "Purchase success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Purchase failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCartFromFirebase(){
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener!!.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onLoadCartFailed(error.message)
                }

            })
    }

    private fun init(){
        cartLoadListener = this
        val layoutManager = LinearLayoutManager(this)
        recyclerCart!!.layoutManager = layoutManager
        recyclerCart!!.addItemDecoration(DividerItemDecoration(this,layoutManager.orientation))
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var sum = 0.0
        for(cartModel in cartModelList!!){
            sum += cartModel!!.totalPrice
        }
        txtTotal.text = StringBuilder("EGP ").append(sum)
        val adapter = MyCartAdapter(this, cartModelList)
        recyclerCart!!.adapter = adapter
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout, message!!, Snackbar.LENGTH_LONG).show()
    }
}