package com.example.popisosnovnihsredstava

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.popisosnovnihsredstava.entities.User
import com.example.popisosnovnihsredstava.helpers.SQLiteSifarnikHelper

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.edittext_username)
        val passwordEditText = findViewById<EditText>(R.id.edittext_password)
        val loginButton = findViewById<Button>(R.id.button_login)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                val sifarnik = SQLiteSifarnikHelper(this)
                val user = sifarnik.getUsers().find { it.username == username }
                if (user != null) {
                    putInPreferences(username, user)
                    Toast.makeText(this, "Uspešan login", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }


                return@setOnClickListener
            } else {
                Toast.makeText(this, "Sva polja moraju biti popunjena!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun putInPreferences(
        user: User
    ) {
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", user.username)
        editor.putInt("id_user", user.id)
        editor.apply()
    }


}
