package com.example.iot

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        val firstname = findViewById<EditText>(R.id.FirstName)
        val lastname = findViewById<EditText>(R.id.LastName)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val submitButton = findViewById<Button>(R.id.SubmitButton)

        val RegistrationManager = Registration.RegistrationManager.getInstance(this)
        RegistrationManager.performRegistration(lastname, firstname, username, password, submitButton, this)


    }

    class RegistrationManager private constructor(context: Context) {
        private val applicationContext = context.applicationContext

        companion object {
            private var instance: RegistrationManager? = null

            fun getInstance(context: Context): RegistrationManager {
                if (instance == null) {
                    instance = RegistrationManager(context)
                }
                return instance as RegistrationManager
            }
        }

        fun performRegistration(
            firstnameEditText: EditText,
            lastnameEditText: EditText,
            usernameEditText: EditText,
            passwordEditText: EditText,
            submitButton: Button,
            context: Context
        ){

            submitButton.setOnClickListener{
                val lastname = lastnameEditText.text.toString()
                val firstname = firstnameEditText.text.toString()
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()

            }
            //add Sqlite code to save information in to database


        }










    }


}