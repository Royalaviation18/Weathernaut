package com.example.weathernaut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.weathernaut.databinding.ActivityHomeBinding
import com.example.weathernaut.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//45078c7afe8248ef93d113741221306
class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by lazy{
        ActivityHomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.weatherapi.com/v1/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData("45078c7afe8248ef93d113741221306","Vadodara",3,"no","no")
        response.enqueue(object: Callback<Weathernaut>{

            override fun onResponse(call: Call<Weathernaut>, response: Response<Weathernaut>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){
                    val temperature = responseBody.current.temp_c.toString()
                    val weatherCondition = responseBody.current.condition.text
                    val forcastDayOne =responseBody.forecast.forecastday
                    binding.tvTemperature.text ="$temperature ℃"
                    binding.tvWeatherCondition.text = "$weatherCondition"

                }

            }

            override fun onFailure(call: Call<Weathernaut>, t: Throwable) {
                Toast.makeText(applicationContext,"No Success",Toast.LENGTH_SHORT).show()
            }

        })
    }
}