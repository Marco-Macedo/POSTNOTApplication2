package com.example.postnotapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class DashboardActivity : AppCompatActivity() {

    private val CHANNEL_ID = "cha"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        var database = FirebaseDatabase.getInstance().reference
        //database.setValue("entrei")

        var getdata = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                for(i in snapshot.children){
                    var state = i.child("ledstate").getValue()
                    sb.append("Estado: $state")
                    if(state == "Ligado")
                    {
                        Toast.makeText(
                            baseContext,
                            "LED ESTÁ LIGADA",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                state.setText(sb)



            }
        }
        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {

            R.id.logout -> {
                /* var token = getSharedPreferences("myKey", Context.MODE_PRIVATE)
                 var editor = token.edit()
                 editor.putString("firebasekey"," ")        // Iguala valor a vazio, fica sem valor, credenciais soltas
                 editor.commit()*/
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}