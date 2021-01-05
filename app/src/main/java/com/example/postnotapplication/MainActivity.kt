package com.example.postnotapplication

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    // Variáveis globais //
    private lateinit var auth: FirebaseAuth
    //  private var loginuser : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        var sharedPref: SharedPreferences? = null

        var arduino =
            FirebaseAuth.AuthStateListener { firebaseAuth ->
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null) {
                    val userEmail = firebaseUser.email


                    var sharedPref = getSharedPreferences("myKey", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("firebasekey", userEmail)
                    editor.commit()
                }
            }





        val firebaseDatabase = FirebaseDatabase.getInstance();
        val reference = firebaseDatabase.getReference()
        reference.child("Arduino").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.children

                children.forEach {
                    Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        )
    }

    fun login(view: View) {
        doLogin()
    }

    private fun doLogin() {
        // VALIDAR EMAIL E PASSWORD


        if(email.text.toString().isEmpty()) {
            email.error = "Please enter email"
            email.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        //*********************************************************************************
        // PASSWORD
        if(password.text.toString().isEmpty()){
            password.error= "Please enter password"
            password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)

                } else {

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    /*public override fun onStart() {
        super.onStart()

            val currentUser = auth.currentUser      // AUTENTICAÇÃO AUTOMATICA
            updateUI(currentUser)           // diz qual o user logado, caso exista...


    }*/

    fun registar(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if(currentUser != null){
          //  if(currentUser.isEmailVerified) {           // EMAIL É VERIFICADO?? Entra na
                startActivity(Intent(this, DashboardActivity::class.java))
                Toast.makeText(this, email.text.toString(), Toast.LENGTH_SHORT).show()
                finish()    // close app
            //}else{
              //  Toast.makeText(
                //    baseContext, "Please verify your email address.",
                  //  Toast.LENGTH_SHORT
                //).show()

        }
    }

}