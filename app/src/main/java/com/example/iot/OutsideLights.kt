package com.example.iot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class OutsideLights :AppCompatActivity() {


    private lateinit var tableLayout: TableLayout
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.outside_lights)

        tableLayout = findViewById(R.id.tableLayout3)

        val db = OutsideLightDBHelper(this)

        val addButton = findViewById<Button>(R.id.AddLight)
        val deleteButton = findViewById<Button>(R.id.deleteOutsideLights)

        addButton.setOnClickListener {
            val light = findViewById<EditText>(R.id.outside_lights_room).text.toString()
            val type = findViewById<EditText>(R.id.type_Of_Light).text.toString()
            db.addOutsideLights(light,type)
            Toast.makeText(this, "LIght added!", Toast.LENGTH_SHORT).show()
            refreshData() // Refresh data after adding a camera
        }

        deleteButton.setOnClickListener {
            val idToDelete = findViewById<EditText>(R.id.outsideLightsIDToDelete).text.toString().toInt()
            db.deleteOutsideLightById(idToDelete)
            Toast.makeText(this, "Light deleted!", Toast.LENGTH_SHORT).show()
            refreshData() // Refresh data after deleting a camera
        }

        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    refreshData() // Periodically refresh data and update TableLayout
                }
            }
        }
        val delayInMillis: Long = 0
        val intervalInMillis: Long = 5000 // Change this interval as needed
        timer.scheduleAtFixedRate(task, delayInMillis, intervalInMillis)
    }

    private fun refreshData() {
        val db = OutsideLightDBHelper(this)
        val lightList = db.getAllOutsideLights()

        // Clear existing rows in the TableLayout
        tableLayout.removeAllViews()

        for (light in lightList) {
            val tableRow = TableRow(this)

            val idTextView = TextView(this)
            idTextView.text = light.id.toString()
            val locationTextView = TextView(this)
            locationTextView.text = light.room
            val angleTextView = TextView(this)
            angleTextView.text = light.type

            tableRow.addView(idTextView)
            tableRow.addView(locationTextView)
            tableRow.addView(angleTextView)

            tableLayout.addView(tableRow)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}