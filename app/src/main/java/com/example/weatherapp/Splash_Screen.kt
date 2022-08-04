package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Splash_Screen: AppCompatActivity() {
    private lateinit var mfusedlocation: FusedLocationProviderClient
    private val myRequestCode=1818
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mfusedlocation = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkpermission()){
            if(isLocationEnable()){
                mfusedlocation.lastLocation.addOnCompleteListener{
                    val location:Location?=it.result
                    if (location==null){
                        Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show()
                    }else{
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                val intent=Intent(this,MainActivity::class.java)
                                intent.putExtra("lat",location.latitude.toString())
                                intent.putExtra("lon",location.longitude.toString())
                                startActivity(intent)
                                finish()
                            },
                            2000
                        )
                    }

                }
            }else{
                //setting
                Toast.makeText(this,"Please Enable Your Location",Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }else{
            //request permission
            requestPermission()
        }
    }




    private fun isLocationEnable(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkpermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
        )
        {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            myRequestCode)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==myRequestCode){
            if (grantResults.isNotEmpty()  &&  grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLocation()
            }
        }
    }
}