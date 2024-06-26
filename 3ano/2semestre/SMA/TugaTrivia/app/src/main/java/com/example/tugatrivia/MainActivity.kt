package com.example.tugatrivia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var selectedDifficulty: String? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve user name from SharedPreferences
        val sharedPref = getSharedPreferences("UserPref", MODE_PRIVATE)
        userName = sharedPref.getString("userName", null)

        val btnStart: Button = findViewById(R.id.btn_start)
        val btnEasy: Button = findViewById(R.id.btn_easy)
        val btnHard: Button = findViewById(R.id.btn_hard)
        val btnLogout: Button = findViewById(R.id.btn_logout)

        btnLogout.setOnClickListener {
            // Clear the stored user name on logout
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnEasy.setOnClickListener {
            selectMode(btnEasy, btnHard)
            selectedDifficulty = "easy"
        }
        btnHard.setOnClickListener {
            selectMode(btnHard, btnEasy)
            selectedDifficulty = "hard"
        }

        btnStart.setOnClickListener {
            if (selectedDifficulty == null) {
                Toast.makeText(this, "Por favor, selecione uma dificuldade", Toast.LENGTH_SHORT)
                    .show()

            } else {
                val questions = when (selectedDifficulty) {
                    "easy" -> Constants.getEasyQuestions()
                    "hard" -> Constants.getHardQuestions()
                    else -> emptyList()
                }
                val intent = Intent(this, QuizQuestionsActivity::class.java)
                intent.putExtra(Constants.USER_NAME, userName)
                intent.putExtra(Constants.SELECTED_DIFFICULTY, selectedDifficulty)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun selectMode(selectedButton: Button, otherButton: Button) {
        selectedButton.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.selected_mode_color)
        otherButton.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.default_mode_color)
    }
}
