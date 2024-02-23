package com.example.weathernaut

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import androidx.core.app.ActivityCompat
import com.example.weathernaut.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED){
            //when permission is already granted

            checkGPS()
        }

        else{
            //when permission is denied
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)        }
    }

    private fun checkGPS() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(
            this.applicationContext
        ).checkLocationSettings(builder.build())

        result.addOnCompleteListener{ task ->
            try {
                //when the GPS is on

                val response = task.getResult(
                    ApiException::class.java
                )
                getUserLocation()
            }catch (e: ApiException){
                //when the GPS is off
                e.printStackTrace()
                when(e.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try{
                            //here we send the request for enabling the GPS
                        val resolveApiException = e as ResolvableApiException
                        resolveApiException.startResolutionForResult(this,200)
                    }catch (sendIntentException: IntentSender.SendIntentException){
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        //when the setting is unavailable
                    }
                }
            }
        }
    }

    private fun getUserLocation() {
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
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener{ task ->
            val location = task.getResult()

            if(location != null){
                try {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val address = geocoder.getFromLocation(location.latitude,location.longitude,1)

                    val address_line = address?.get(0)?.getAddressLine(0)
                    binding.tvLocation.text = address_line

                    val address_location = address?.get(0)?.getAddressLine(0)
//                    openLocation(address_location.toString())

                }catch (e: IOException){

                }
            }
        }
    }

//    private fun openLocation(location: String) {
//        //here we open this location in go
//    }
}