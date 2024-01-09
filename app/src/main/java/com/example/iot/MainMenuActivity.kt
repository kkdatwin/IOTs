package com.example.iot

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_main)

        val CamerasButton = findViewById<Button>(R.id.Cameras)
        val InsideLightsButton = findViewById<Button>(R.id.InSideLights)
        val OutsideLightsButton = findViewById<Button>(R.id.OutSideLights)
        val DevicesButton = findViewById<Button>(R.id.Devices)
        val LockDoorsButton = findViewById<Button>(R.id.LockDoors)

        // Click listeners for each button
        CamerasButton.setOnClickListener {
            startActivity(Intent(this, Cameras::class.java))
        }

        InsideLightsButton.setOnClickListener {
            startActivity(Intent(this, InsideLights::class.java))
        }

        OutsideLightsButton.setOnClickListener {
            startActivity(Intent(this, OutsideLights::class.java))
        }

        DevicesButton.setOnClickListener {
            startActivity(Intent(this, Devices::class.java))
        }

        LockDoorsButton.setOnClickListener {
            startActivity(Intent(this, LockDoors::class.java))
        }
    }



}