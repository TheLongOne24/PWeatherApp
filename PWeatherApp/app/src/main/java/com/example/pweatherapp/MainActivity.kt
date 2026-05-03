package com.example.pweatherapp

import android.os.AsyncTask
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pweatherapp.adapter.RvAdapter
import com.example.pweatherapp.extra.Switcher


class MainActivity : ComponentActivity() {

    private val API: String = "7ea6ca71d85b6877df16ddbecb6d8591"
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cityInput = findViewById<EditText>(R.id.et_city)

        cityInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val city = cityInput.text.toString().trim()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(cityInput.windowToken, 0)
                if (city.isNotEmpty()) {
                    weatherTask().execute(city)
                }
                true
            } else {
                false
            }
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        weatherTask().execute("София")
    }
    inner class weatherTask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            val city = params[0]
            var response: String?
            var forecastResponse: String?
            try
            {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&lang=bg&appid=$API")
                    .readText(Charsets.UTF_8)
                forecastResponse = URL("https://api.openweathermap.org/data/2.5/forecast?q=$city&units=metric&lang=bg&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e: Exception)
            {
                response = null
                forecastResponse = null
            }
            return response + "###" + forecastResponse
        }

        override fun onPostExecute(result: String?) {
            if (result == null) {
                findViewById<TextView>(R.id.errortext).apply {
                    text = "Градът не е намерен"
                    visibility = View.VISIBLE
                }
                return
            }
            super.onPostExecute(result)
            try {
                val bottomSheet = findViewById<View>(R.id.bottomSheet)
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.peekHeight = 200
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED

                val parts = result!!.split("###")
                val weatherJson = JSONObject(parts[0])
                val forecastJson = JSONObject(parts[1])

                //Forecast Weather start-------------------------------------------------------------------------------------------------------------------------------

                val forecastList = ArrayList<Array<String>>()
                val recyclerView = findViewById<RecyclerView>(R.id.rv_forecast)

                val list = forecastJson.getJSONArray("list")

                for (i in 0 until list.length()) {
                    val item = list.getJSONObject(i)

                    val temp = item.getJSONObject("main")
                        .getDouble("temp")
                        .toInt()
                        .toString()

                    val description = item.getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("description")

                    val icon = item.getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("icon")

                    val time = item.getString("dt_txt")

                    forecastList.add(arrayOf(temp, description, time, icon))
                }

                recyclerView.layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                recyclerView.adapter = RvAdapter(forecastList)

                //Forecast Weather end-------------------------------------------------------------------------------------------------------------------------------

                //Current Weather start-------------------------------------------------------------------------------------------------------------------------------
                val currentweather = JSONObject(weatherJson.toString())
                val main = currentweather.getJSONObject("main")
                val sys = currentweather.getJSONObject("sys")
                val wind = currentweather.getJSONObject("wind")
                val weather = currentweather.getJSONArray("weather").getJSONObject(0)
                val clouds = currentweather.getJSONObject("clouds")
                val updatedAt: Long = currentweather.getLong("dt")
                val updatedAtText = "Обновено: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt * 1000))
                val temp = round(main.getString("temp").toFloat()).toInt()
                val temp_final = temp.toString() + "°C"
                val feels_like = round(main.getString("feels_like").toFloat()).toInt()
                val feels_like_final = "Усеща се като: " + feels_like.toString() + "°C"
                val clouds_all = clouds.getString("all") + " %"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = currentweather.getString("name") + ", " + sys.getString("country")
                val image = weather.getString("icon")

                findViewById<ImageView>(R.id.weather_image).setImageResource(Switcher(image))
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp_final
                findViewById<TextView>(R.id.feels_like).text = feels_like_final
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed + " m/s WNW"
                findViewById<TextView>(R.id.pressure).text = pressure + " hPa"
                findViewById<TextView>(R.id.humidity).text = humidity + " %"
                findViewById<TextView>(R.id.cloudy).text = clouds_all
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
                //Current Weather end-------------------------------------------------------------------------------------------------------------------------------
            }
            catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
            }

        }
    }
}
