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

class Devices: AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.devices)

        tableLayout = findViewById(R.id.tableLayout1)

        val db = DeviceDBHelper(this)

        val addButton = findViewById<Button>(R.id.AddDevice)
        val deleteButton = findViewById<Button>(R.id.deleteDevices)

        addButton.setOnClickListener {
            val makeMode = findViewById<EditText>(R.id.Add_device_MM).text.toString()
            val owner = findViewById<EditText>(R.id.deviceOwner).text.toString()
            db.addDevice(makeMode, owner)
            Toast.makeText(this, "Device added!", Toast.LENGTH_SHORT).show()
            refreshData() // Refresh data after adding a camera
        }

        deleteButton.setOnClickListener {
            val idToDelete = findViewById<EditText>(R.id.DeviceIDToDelete).text.toString().toInt()
            db.deleteDeviceById(idToDelete)
            Toast.makeText(this, "Camera deleted!", Toast.LENGTH_SHORT).show()
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
        val db = DeviceDBHelper(this)
        val deviceList = db.getAllDevices()

        // Clear existing rows in the TableLayout
        tableLayout.removeAllViews()

        for (device in deviceList) {
            val tableRow = TableRow(this)

            val idTextView = TextView(this)
            idTextView.text = device.id.toString()
            val locationTextView = TextView(this)
            locationTextView.text = device.makeModel
            val angleTextView = TextView(this)
            angleTextView.text = device.Owner

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