package com.example.postnotapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    // Variáveis globais //
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

    }

    fun btnregistar(view: View)
    {
       signUpUser()
    }

    private fun signUpUser() {

        // Verifiar se o email é valido ou nao //

        if(registaremail.text.toString().isEmpty()) {
            registaremail.error = "Please enter email"
            registaremail.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(registaremail.text.toString()).matches()){
            registaremail.error = "Please enter valid email"
            registaremail.requestFocus()
            return
        }

        //*********************************************************************************
        // PASSWORD
        if(registarpassword.text.toString().isEmpty()){
            registarpassword.error= "Please enter password"
            registarpassword.requestFocus()
            return
        }

        //*********************************************************************************
        // CRIAR REGISTO

        auth.createUserWithEmailAndPassword(
            registaremail.text.toString(),
            registarpassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user!!.sendEmailVerification()          // envia um email de verificacao
                       .addOnCompleteListener { task ->
                            if (task.isSuccessful) {            // email verificado? volta para a mainactivity e e-mail está agora verificado
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext,
                        "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}