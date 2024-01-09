package com.example.iot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LockDoors :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lockdoors)

        val frontDoorUnlockedButton = findViewById<Button>(R.id.frontlockdoorb)
        val frontDoorlockedButton = findViewById<Button>(R.id.frontLockDoorsb)
        val backDoorUnlockedButton = findViewById<Button>(R.id.backlockdoorb)
        val backDoorlockedButton = findViewById<Button>(R.id.backLockDoorsb)


        frontDoorUnlockedButton.setOnClickListener {

            Toast.makeText(this, "Front Door Unlocked!", Toast.LENGTH_SHORT).show()
        }
        frontDoorlockedButton.setOnClickListener {

            Toast.makeText(this, "Front Door locked!", Toast.LENGTH_SHORT).show()
        }
        backDoorUnlockedButton.setOnClickListener {

            Toast.makeText(this, "Back Door Unlocked!!", Toast.LENGTH_SHORT).show()
        }
        backDoorlockedButton.setOnClickListener {

            Toast.makeText(this, "Back Door locked!", Toast.LENGTH_SHORT).show()
        }



    }}