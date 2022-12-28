package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class mapActivity : AppCompatActivity(), OnMapReadyCallback{

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap
    private lateinit var backButton: Button

    var database = FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity)

        backButton = findViewById(R.id.backButton)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@OnMapReadyCallback
            }
            googleMap.isMyLocationEnabled = true
            val location = LatLng(29.986618,31.439071)
            googleMap.addMarker(MarkerOptions().position(location).title("My location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 5f))

            database.child("Locations").addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (s in snapshot.children){
                        val lat = s.child("lat").value as Double
                       val long =  s.child("long").value as Double

                        val loc= LatLng(lat , long)

                        googleMap.addMarker(MarkerOptions().position(location).title("Supermarket"))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 5f))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        })

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

}


