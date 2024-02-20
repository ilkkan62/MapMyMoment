package com.example.myapplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import android.widget.TextView
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class NoteEditActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    private var noteDao: NoteDao? = null
    private var note: Note? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    //location provider
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvCurrentLocation: TextView
    private val locationList = mutableListOf<Location>()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        // Set toolbar
        setSupportActionBar(findViewById(R.id.tbEdit))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        // Find views by Id
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSave = findViewById<Button>(R.id.btnSave)
        tvCurrentLocation = findViewById<TextView>(R.id.tvCurrentLocation)

        // Provider for GPS
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Permission check GPS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission denied
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Permission already successfull
            getLocation()
        }

        // Initialize Room DB
        val db = Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java, "notes"
        ).allowMainThreadQueries().build()
        noteDao = db.noteDao()

        // Get note id from Intent
        val id = intent.getLongExtra("id", -1)
        if (id >= 0) {
            note = noteDao!!.loadAllByIds(id.toLong())[0]
            etTitle?.setText(note?.title)
            etMessage?.setText(note?.message)
            latitude = note?.latitude ?: 0.0
            longitude = note?.longitude ?: 0.0

        }

        // Set OnClickListener
        btnSave.setOnClickListener{
            val title = etTitle?.text.toString()
            val message = etMessage?.text.toString()

            if (note != null) {
                note!!.title = title
                note!!.message = message
                note!!.latitude = latitude
                note!!.longitude = longitude
                noteDao?.update(note!!)
            } else {
                noteDao!!.insertAll(Note(title, message, latitude, longitude))
            }

            // Show toast for user
            Toast.makeText(this, noteDao!!.getAll().toString(), Toast.LENGTH_LONG).show()

            finish()
        }
    }

    private fun getLocation() {
        // Last location place
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {

                    val latitude = location.latitude
                    val longitude = location.longitude

                    tvCurrentLocation.text = "Current Location: $latitude, $longitude"

                    locationList.add(location)

                    this.latitude = latitude
                    this.longitude = longitude
                } else {
                    Toast.makeText(this, R.string.location_null, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted
                    getLocation()
                } else {
                    // Permission has been denied
                    Toast.makeText(this, R.string.location_denied, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.delete -> showDeleteDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.delete_message))
            .setPositiveButton(getString(R.string.yes), this)
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        note?.let {
            noteDao?.delete(it)

            // Display Toast
            Toast.makeText(this, R.string.delete_message, Toast.LENGTH_LONG).show()

            finish()
        }
    }
}