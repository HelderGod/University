package com.example.tugatrivia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var emailTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var Btn: Button
    private lateinit var progressbar: ProgressBar

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailTextView = findViewById(R.id.email)
        passwordTextView = findViewById(R.id.password)
        Btn = findViewById(R.id.login)
        progressbar = findViewById(R.id.progressBar)

        Btn.setOnClickListener {
            loginUserAccount()
        }
    }

    private fun loginUserAccount() {
        progressbar.visibility = View.VISIBLE

        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Por favor, preencha todos os campos!",
                Toast.LENGTH_LONG
            ).show()
            progressbar.visibility = View.GONE
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid
                    userId?.let {
                        db.collection("users").document(it).get()
                            .addOnSuccessListener { document ->
                                val userName = document.getString("name") ?: ""

                                // Salva o nome do utilizador no SharedPreferences
                                val sharedPref =
                                    getSharedPreferences("UserPref", MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("userName", userName)
                                editor.apply()

                                // Inicia a MainActivity
                                val intent = Intent(
                                    this@LoginActivity,
                                    MainActivity::class.java
                                )
                                intent.putExtra("userName", userName)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    applicationContext,
                                    "Erro ao obter dados do utilizador: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressbar.visibility = View.GONE
                            }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Login falhou! Verifique os seus dados e tente novamente.",
                        Toast.LENGTH_LONG
                    ).show()
                    progressbar.visibility = View.GONE
                }
            }
    }
}
