package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val apiKey = "3ec6604830f5fdb0a49d66017370daf4"
    // apiKey="3ec6604830f5fdb0a49d66017370daf4"
    // base url=https://api.openweathermap.org/data/2.5/weatherlat={lat}&lon={lon}&exclude={part}&appid={API key}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lat=intent.getStringExtra("lat")
        val lon=intent.getStringExtra("lon")
        getData(lat,lon)
    }

    private fun getData(lat: String?, lon: String?) {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${apiKey}"
        Log.d("api", url)
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response->
                setdata(response)
            },
            {
                Log.d("api failed",it.toString())
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    @SuppressLint("SetTextI18n")
    private fun setdata(response: JSONObject) {
        val imgId=response.getJSONArray("weather").getJSONObject(0).getString("icon")
        Glide.with(this).load("https://openweathermap.org/img/wn/${imgId}@2x.png").into(weatherImg)
        city.text=response.getString("name")
        weatherType.text = response.getJSONArray("weather").getJSONObject(0).getString("main")
        lat_lon.text= response.getJSONObject("coord").getString("lat")+","+response.getJSONObject("coord").getString("lon")
        var ctemp = response.getJSONObject("main").getString("temp")
        ctemp = (((ctemp.toFloat() - 273.15)).toInt()).toString()
        temp.text = "$ctemp°C"
        val calSunrise = response.getJSONObject("sys").getLong("sunrise")
        sunrise.text =
            "Sunrise\n" + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(calSunrise * 1000)
        val calSunset = response.getJSONObject("sys").getLong("sunset")
        sunset.text =
            "Sunset\n" + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(calSunset * 1000)
        wind.text ="Wind\n"+ response.getJSONObject("wind").getString("speed") + " m/s"
        humudity.text = "Humudity\n"+response.getJSONObject("main").getString("humidity")+"%"
        pressure.text = "Pressure\n"+response.getJSONObject("main").getString("pressure")
        var minTemp = response.getJSONObject("main").getString("temp_min")
        minTemp = (((minTemp.toFloat() - 273.15)).toInt()).toString()
        min_temp.text = "Min Temp\n$minTemp°C"
        var maxTemp = response.getJSONObject("main").getString("temp_max")
        maxTemp = (((maxTemp.toFloat() - 273.15)).toInt()).toString()
        max_temp.text = "Max Temp\n$maxTemp°C"
        var feelsLike = response.getJSONObject("main").getString("feels_like")
        feelsLike = (((feelsLike.toFloat() - 273.15)).toInt()).toString()
        feels_like.text = "Feels Like\n$feelsLike°C"
    }
}


