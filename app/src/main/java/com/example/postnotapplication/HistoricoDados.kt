package com.example.postnotapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_historico_dados.*
import org.w3c.dom.Text
import java.lang.StringBuilder

class HistoricoDados : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var database = FirebaseDatabase.getInstance().reference
        setContentView(R.layout.activity_historico_dados)
        auth = FirebaseAuth.getInstance()

        var getdata = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                for(i in snapshot.children){

                    var dia = i.child("dia").getValue()
                    var mes = i.child("mes").getValue()
                    var hora = i.child("hora").getValue()
                    var minutos = i.child("minutos").getValue()
                    sb.append("$hora"+"h"+"$minutos : $dia $mes -> Aberto\n")
                }
                TextView.setText(sb)
            }
        }
        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

    }
}