package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weatherText: TextView
    private lateinit var fetchWeatherButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        weatherText = findViewById(R.id.weatherText)
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton)

        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)

        val dateTextView = findViewById<TextView>(R.id.xml_text_date)
        dateTextView.text = dateFormat

        // Rotate Icon
        val ivIcon = findViewById<ImageView>(R.id.ivIcon)
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        ivIcon.startAnimation(rotation)

        // Weather button
        fetchWeatherButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fetchWeatherWithLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
        }

        val myButton = findViewById<Button>(R.id.myButton)
        myButton.setOnClickListener {
            // Show Signed-In-Message
            Toast.makeText(this, getString(R.string.signed_in), Toast.LENGTH_LONG).show()

            // Open ListActivity
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchWeatherWithLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude

                    if (latitude != null && longitude != null) {
                        FetchWeatherTask().execute(latitude, longitude)
                    } else {
                        Toast.makeText(
                            this,
                            "Standortkoordinaten nicht verfügbar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } ?: run {
                    Toast.makeText(this, "Standort nicht verfügbar", Toast.LENGTH_SHORT).show()
                }
            }
    }

    inner class FetchWeatherTask : AsyncTask<Double, Void, String>() {
        override fun doInBackground(vararg params: Double?): String {
            val apiKey = "08366c14ec2a55b38ce6e34dce4d4edd"
            val latitude = params[0]
            val longitude = params[1]

            val url =
                URL("http://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric")
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    result.append(line)
                }
                return result.toString()
            } finally {
                urlConnection.disconnect()
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val json = JSONObject(result)
            val main = json.getJSONObject("main")
            val temperature = main.getDouble("temp")
            val weather =
                json.getJSONArray("weather").getJSONObject(0).getString("description")


            weatherText.text = "Wetterabfrage: $temperature°C, $weather"
        }
    }
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}