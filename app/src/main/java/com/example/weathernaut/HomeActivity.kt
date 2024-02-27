package com.example.weathernaut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.weathernaut.databinding.ActivityHomeBinding
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
        binding.btnMore.setOnClickListener {
            val intent = Intent(applicationContext,MoreDetailsActivity::class.java)
            startActivity(intent)
        }
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
                    changeWeatherIcon(weatherConditionDayOne, binding.ivToday)

                    val forecastDayTwo = forecastDayList[1].date
                    val minTempDayTwo = forecastDayList[1].day.mintemp_c.toInt()
                    val maxTempDayTwo = forecastDayList[1].day.maxtemp_c.toInt()
                    val weatherConditionDayTwo = forecastDayList[1].day.condition.text
                    changeWeatherIcon(weatherConditionDayTwo, binding.ivTomorrow)

                    val forecastDayThree = forecastDayList[2].date
                    val minTempDayThree = forecastDayList[2].day.mintemp_c.toInt()
                    val maxTempDayThree = forecastDayList[2].day.maxtemp_c.toInt()
                    val weatherConditionDayThree = forecastDayList[2].day.condition.text
                    val date =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(forecastDayThree)
                    val dayOfWeek = getDayOfWeek(date)
                    changeWeatherIcon(weatherConditionDayThree, binding.ivDayAfter)

                    binding.tvTemperature.text = "$temperature° C"
                    binding.tvWeatherCondition.text = "$weatherCondition"
                    binding.tvCityName.text = "$cityName"
                    binding.tvMax.text = "$maxTempDayOne° /"
                    binding.tvMin.text = "$minTempDayOne°"
                    binding.tvCondition.text = "$weatherConditionDayOne"


                    binding.tvTmMax.text = "$maxTempDayTwo° /"
                    binding.tvTmMin.text = "$minTempDayTwo°"
                    binding.tvTmCondition.text = "$weatherConditionDayTwo"


                    binding.tvDayAfterMax.text = "$maxTempDayThree° /"
                    binding.tvDayAfterMin.text = "$minTempDayThree°"
                    binding.tvDayAfterCondtion.text = "$weatherConditionDayThree"
                    binding.tvDayAfter.text = "$dayOfWeek"

                    changeMainWeatherImage(weatherCondition)
                }

            }

            override fun onFailure(call: Call<Weathernaut>, t: Throwable) {
                Toast.makeText(applicationContext, "No Success", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun changeWeatherIcon(weatherCondition: String, imageView: ImageView) {
        when (weatherCondition) {
            "Haze" -> {
                imageView.setImageResource(R.drawable.haze)
            }

            "Clouds", "Partly Clouds", "Overcast", "Mist", "Foggy" -> {
                imageView.setImageResource(R.drawable.partly_cloudy)
            }

            "Clear Sky", "Sunny", "Clear" -> {
                imageView.setImageResource(R.drawable.sun)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                imageView.setImageResource(R.drawable.d_rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                imageView.setImageResource(R.drawable.snow)
            }

            else -> {
                imageView.setImageResource(R.drawable.sun)
            }

        }
    }

    private fun changeMainWeatherImage(weatherCondition: String) {
        when (weatherCondition) {
            "Haze" -> {
                binding.ivMainWeather.setImageResource(R.drawable.haze_day)
            }

            "Clouds", "Partly Clouds", "Overcast", "Mist", "Foggy" -> {
                binding.ivMainWeather.setImageResource(R.drawable.cloudy_day)
            }

            "Clear Sky", "Clear" -> {
                binding.ivMainWeather.setImageResource(R.drawable.clear_day)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.ivMainWeather.setImageResource(R.drawable.rainy_day)
            }

            "Light Snow", "Moderate Snow", "Hesavy Snow", "Blizzard" -> {
                binding.ivMainWeather.setImageResource(R.drawable.snow_day)
            }

            "Sunny" -> {
                binding.ivMainWeather.setImageResource(R.drawable.sunny_day)
            }

            else -> {
                binding.ivMainWeather.setImageResource(R.drawable.sunny_day)
            }

        }

    }

    private fun getDayOfWeek(date: Date?): Any {
        val calendar = Calendar.getInstance()
        date?.let { calendar.time = it }

        // Format the day of the week
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayFormat.format(calendar.time)
    }
}
