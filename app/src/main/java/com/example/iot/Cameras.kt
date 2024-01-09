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

class Cameras : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cameras)

        tableLayout = findViewById(R.id.tableLayout)

        val db = CameraDBHelper(this)

        val addButton = findViewById<Button>(R.id.AddCameras)
        val deleteButton = findViewById<Button>(R.id.deleteCameras)

        addButton.setOnClickListener {
            val description = findViewById<EditText>(R.id.Add_cameras_loc).text.toString()
            val angle = findViewById<EditText>(R.id.camera_angle).text.toString()
            db.addCamera(description, angle)
            Toast.makeText(this, "Camera added!", Toast.LENGTH_SHORT).show()
            refreshData() // Refresh data after adding a camera
        }

        deleteButton.setOnClickListener {
            val idToDelete = findViewById<EditText>(R.id.CameraIDToDelete).text.toString().toInt()
            db.deleteCameraById(idToDelete)
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
        val db = CameraDBHelper(this)
        val cameraList = db.getAllCameras()

        // Clear existing rows in the TableLayout
        tableLayout.removeAllViews()

        for (camera in cameraList) {
            val tableRow = TableRow(this)

            val idTextView = TextView(this)
            idTextView.text = camera.id.toString()
            val locationTextView = TextView(this)
            locationTextView.text = camera.location
            val angleTextView = TextView(this)
            angleTextView.text = camera.angle

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