package com.charge.ysweatherapp

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var locationStore = "Alwar"
    private lateinit var temp_c: TextView
    private lateinit var date: TextView
    private lateinit var day: TextView
    private lateinit var temp_f: TextView
    private lateinit var location: TextView
    private lateinit var condition: TextView
    private lateinit var humidity: TextView
    private lateinit var image: ImageView
    private lateinit var city: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var search : ImageView
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Load Weather")
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        init_var()
        getWeather()
        search.setOnClickListener {
            getCity()
        }
    }


    private fun getCity() {
        locationStore =  city.text.toString()
        getWeather()
    }

    private fun init_var() {
        date = findViewById(R.id.tvMainDateTime)
        day = findViewById(R.id.tvMainDay)
        location = findViewById(R.id.tvMainLocation)
        temp_c = findViewById(R.id.tvMainTempC)
        temp_f = findViewById(R.id.tvMainTempF)
        condition = findViewById(R.id.tvMainCondition)
        humidity = findViewById(R.id.tvMainHumidity)
        image = findViewById(R.id.ivMainImage)
        city = findViewById(R.id.editTextTextPersonName)
        recyclerView = findViewById(R.id.rvMainForecastShow)
        search = findViewById(R.id.imageView2)
    }

    private fun getWeather() {
        val url = "http://api.weatherapi.com/v1/forecast.json?key=355f3050b97647c7b2b161028231903&q="+locationStore+"&days=1&aqi=yes&alerts=yes"
        val requestQueue : RequestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                // Do something with the response
                temp_c.text= response.getJSONObject("current").getString("temp_c")
                temp_f.text= response.getJSONObject("current").getString("temp_f")
                date.text= response.getJSONObject("current").getString("last_updated")
                humidity.text= response.getJSONObject("current").getString("humidity")+"%"
                condition.text= response.getJSONObject("current").getJSONObject("condition").getString("text")
                val icon = response.getJSONObject("current").getJSONObject("condition").getString("icon")
                Picasso.get().load(Uri.parse(icon)).placeholder(R.drawable.weather_forecast).into(image)

                if (response.getJSONObject("current").getString("is_day")=="0"){
                    day.text = "Night"
                }
                else{
                    day.text = "Day"
                }

                location.text = response.getJSONObject("location").getString("name")
                var i =0
                val list1 = mutableListOf<model>()
                while (i<response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").length()){
                    val obj : JSONObject= response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i)
                    val model1 = model(obj.getString("time").takeLast(6),obj.getString("temp_c"),obj.getString("humidity"),obj.getJSONObject("condition").getString("text"))
                    list1.add(model1)
                    i++
                }
                val adapter1 = WeatherAdapter(list1)
                recyclerView.adapter = adapter1
                recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
                val currentTime = System.currentTimeMillis()
                val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val timeIn24HourFormat = dateFormat.format(currentTime)
                recyclerView.scrollToPosition(timeIn24HourFormat.substring(0,2).toInt())
                progressDialog.dismiss()

            },
            {
                // Handle any errors
            })
        requestQueue.add(jsonObjectRequest)

    }
}