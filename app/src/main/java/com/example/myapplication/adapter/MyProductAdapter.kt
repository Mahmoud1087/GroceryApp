package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.eventBus.UpdateCartEvent
import com.example.myapplication.listener.ICartLoadListener
import com.example.myapplication.listener.IRecyclerClickListener
import com.example.myapplication.model.CartModel
import com.example.myapplication.model.product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.greenrobot.eventbus.EventBus

class MyProductAdapter(
    private val context:Context,
    private val list:List<product>,
    private val cartListner:ICartLoadListener

): RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder>() {

    class MyProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView?=null
        var name: TextView?=null
        var price: TextView?=null
        var quantity: TextView?=null

        private var clickListener: IRecyclerClickListener? = null

        fun setClickListener(clickListener: IRecyclerClickListener){
            this.clickListener = clickListener;
        }

        init {
            imageView = itemView.findViewById(R.id.imageView) as ImageView;
            name = itemView.findViewById(R.id.name) as TextView;
            price = itemView.findViewById(R.id.price) as TextView;
            quantity = itemView.findViewById(R.id.quantity) as TextView;

            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v,adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductViewHolder {
        return MyProductViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_product_item, parent, false))

    }

    override fun onBindViewHolder(holder: MyProductViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position].image)
            .into(holder.imageView!!)
        holder.name!!.text = StringBuilder().append(list[position].name)
        holder.price!!.text = StringBuilder("EGP ").append(list[position].price)
        holder.quantity!!.text = StringBuilder("Left in stock: ").append(list[position].quantity)

        holder.setClickListener(object : IRecyclerClickListener{
            override fun onItemClickListener(view: View?, position: Int) {
                addToCart(list[position])
            }

        })
    }

    private fun addToCart(productModel: product) {
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())

        userCart.child(productModel.key!!)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val cartModel = snapshot.getValue(CartModel::class.java)
                        val updateData: MutableMap<String,Any> = HashMap()
                        cartModel!!.quantityOfBuy = cartModel!!.quantityOfBuy+1;
                        updateData["quantityOfBuy"] = cartModel!!.quantityOfBuy
                        updateData["totalPrice"] =
                            cartModel!!.quantityOfBuy * cartModel.price!!.toFloat()

                        userCart.child(productModel.key!!)
                            .updateChildren(updateData)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartListner.onLoadCartFailed("Added to cart")
                            }
                            .addOnFailureListener{ e-> cartListner.onLoadCartFailed(e.message)}
                    }
                    else{
                        val cartModel = CartModel()
                        cartModel.key = productModel.key
                        cartModel.name = productModel.name
                        cartModel.image = productModel.image
                        cartModel.price = productModel.price
                        cartModel.quantityOfBuy = 1
                        cartModel.totalPrice = productModel.price!!.toFloat()

                        userCart.child(productModel.key!!)
                            .setValue(cartModel)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartListner.onLoadCartFailed("Added to cart")
                            }
                            .addOnFailureListener{ e-> cartListner.onLoadCartFailed(e.message)}

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    cartListner.onLoadCartFailed(error.message)
                }

            })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}