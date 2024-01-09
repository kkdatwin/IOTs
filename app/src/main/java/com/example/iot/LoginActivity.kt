package com.example.iot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)


        val loginManager = LoginManager.getInstance(this)
        loginManager.performLogin(username, password, loginButton, this)



//        val registrationButton = findViewById<Button>(R.id.RegistrationButton)
//        registrationButton.setOnClickListener {
//            startActivity(Intent(this, Registration::class.java))
//        }

    }


    class LoginManager private constructor(context: Context) {
        private val applicationContext = context.applicationContext

        companion object {
            private var instance: LoginManager? = null

            fun getInstance(context: Context): LoginManager {
                if (instance == null) {
                    instance = LoginManager(context)
                }
                return instance as LoginManager
            }
        }

        fun performLogin(
            usernameEditText: EditText,
            passwordEditText: EditText,
            loginButton: Button,
            context: Context
        ) {
            loginButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (isValidCredentials(username, password)) {
                    // Login Successful
                    Toast.makeText(applicationContext, "Login Successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainMenuActivity::class.java)
                    context.startActivity(intent)
                } else {
                    // Login Failed
                    Toast.makeText(applicationContext, "Login Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun isValidCredentials(username: String, password: String): Boolean {
            // Implement your validation logic here
            // change this code to check if the username and password is in sqlite database
            return username == "user" && password == "1234"
        }
    }
}