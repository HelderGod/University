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

class RegistrationActivity : AppCompatActivity() {

    private lateinit var emailTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var nameTextView: EditText
    private lateinit var Btn: Button
    private lateinit var progressbar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailTextView = findViewById(R.id.email)
        passwordTextView = findViewById(R.id.passwd)
        nameTextView = findViewById(R.id.name)
        Btn = findViewById(R.id.btnregister)
        progressbar = findViewById(R.id.progressbar)

        Btn.setOnClickListener {
            registerNewUser()
        }

    }

    private fun registerNewUser() {
        progressbar.visibility = View.VISIBLE

        val name = nameTextView.text.toString()
        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Por favor, preencha todos os campos!",
                Toast.LENGTH_LONG
            ).show()
            progressbar.visibility = View.GONE
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid
                    if (userId != null) {
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email
                        )
                        db.collection("users").document(userId).set(user)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    //Log.d("RegistrationActivity", "Dados do utilizador guardados")
                                    Toast.makeText(
                                        applicationContext,
                                        "Registo bem-sucedido!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    progressbar.visibility = View.GONE
                                    goToMainActivity(name)
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Falha ao registar. Verifique os seus dados e tente novamente.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    progressbar.visibility = View.GONE
                                }
                            }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Falha ao registar. Verifique seus dados e tente novamente.",
                            Toast.LENGTH_LONG
                        ).show()
                        progressbar.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Falha ao registar. Verifique os seus dados e tente novamente.",
                        Toast.LENGTH_LONG
                    ).show()
                    progressbar.visibility = View.GONE
                }
            }
    }

    private fun goToMainActivity(userName: String) {
        val sharedPref = getSharedPreferences("UserPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("userName", userName)
        editor.apply()

        // Crie um Intent para iniciar a MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userName", userName)
        startActivity(intent)
        finish()  // Finaliza a RegistrationActivity e inicia a MainActivity
    }
}
