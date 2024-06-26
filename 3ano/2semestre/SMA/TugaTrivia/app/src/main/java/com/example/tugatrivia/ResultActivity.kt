package com.example.tugatrivia

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvName: TextView = findViewById(R.id.tv_name)
        val tvScore: TextView = findViewById(R.id.tv_score)
        val btnFinish: TextView = findViewById(R.id.btn_finish)
        val tvCongratulations: TextView = findViewById(R.id.tv_congratulations)

        tvName.text = intent.getStringExtra(Constants.USER_NAME)

        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)

        if (correctAnswers == 0) {
            tvCongratulations.text = "Não foi desta vez! "
        } else if (correctAnswers in 1..4) { // Corrigido: 1, 4
            tvCongratulations.text = "Podia ter sido melhor!"
        } else if (correctAnswers in 5..7) { // Corrigido: 6, 7
            tvCongratulations.text = "Bom trabalho!"
        } else if (correctAnswers in 8..9) { // Corrigido: 8, 9
            tvCongratulations.text = "Excelente!"
        } else {
            tvCongratulations.text = "Perfeito! Parabéns!!"
        }
        tvScore.text = "Acertaste $correctAnswers de $totalQuestions!"

        btnFinish.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}