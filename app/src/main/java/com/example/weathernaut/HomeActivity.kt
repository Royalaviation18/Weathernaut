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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


//45078c7afe8248ef93d113741221306
class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    val cityName = "Vadodara"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData()
    }

    private fun fetchWeatherData() {

        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.weatherapi.com/v1/")
            .build().create(ApiInterface::class.java)
        val response =
            retrofit.getWeatherData("45078c7afe8248ef93d113741221306", cityName, 3, "no", "no")
        response.enqueue(object : Callback<Weathernaut> {

            override fun onResponse(call: Call<Weathernaut>, response: Response<Weathernaut>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.current.temp_c.toString()
                    val weatherCondition = responseBody.current.condition.text

                    val forecastDayList = responseBody.forecast.forecastday
                    val forecastDayOne = forecastDayList[0].date
                    val minTempDayOne = forecastDayList[0].day.mintemp_c.toInt()
                    val maxTempDayOne = forecastDayList[0].day.maxtemp_c.toInt()
                    val weatherConditionDayOne = forecastDayList[0].day.condition.text

                    val forecastDayTwo = forecastDayList[1].date
                    val minTempDayTwo = forecastDayList[1].day.mintemp_c.toInt()
                    val maxTempDayTwo = forecastDayList[1].day.maxtemp_c.toInt()
                    val weatherConditionDayTwo = forecastDayList[1].day.condition.text

                    val forecastDayThree = forecastDayList[2].date
                    val minTempDayThree = forecastDayList[2].day.mintemp_c.toInt()
                    val maxTempDayThree = forecastDayList[2].day.maxtemp_c.toInt()
                    val weatherConditionDayThree = forecastDayList[2].day.condition.text
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(forecastDayThree)
                    val dayOfWeek = getDayOfWeek(date)

                    binding.tvTemperature.text = "$temperature ℃"
                    binding.tvWeatherCondition.text = "$weatherCondition"
                    binding.tvCityName.text = "$cityName"
                    binding.tvMax.text = "$maxTempDayOne° /"
                    binding.tvMin.text = "$minTempDayOne°"
                    binding.tvCondition.text ="$weatherConditionDayOne"

                    binding.tvTmMax.text ="$maxTempDayTwo° /"
                    binding.tvTmMin.text="$minTempDayTwo°"
                    binding.tvTmCondition.text="$weatherConditionDayTwo"

                    binding.tvDayAfterMax.text = "$maxTempDayThree° /"
                    binding.tvDayAfterMin.text = "$minTempDayThree°"
                    binding.tvDayAfterCondtion.text="$weatherConditionDayThree"
                    binding.tvDayAfter.text= "$dayOfWeek"
                }

            }

            override fun onFailure(call: Call<Weathernaut>, t: Throwable) {
                Toast.makeText(applicationContext, "No Success", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDayOfWeek(date: Date?): String {
        val calendar = Calendar.getInstance()
        date?.let { calendar.time = it }

        // Format the day of the week
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayFormat.format(calendar.time)
    }
}