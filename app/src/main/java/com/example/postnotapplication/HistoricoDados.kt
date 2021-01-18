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


                    var Janeiro = snapshot.child("Atualizado/2021/janeiro").getValue()
                    var Fevereiro = snapshot.child("Atualizado/2021/fevereiro").getValue()

                //var mes = snapshot.child("Historico/mes").getValue()
                    //var hora = snapshot.child("Historico/hora").getValue()
                    //var minutos = snapshot.child("Historico/minutos").getValue()
                    //sb.append("$hora"+"h"+"$minutos : $dia $mes -> Aberto\n")
                if(Janeiro != null)
                {
                    sb.append("$Janeiro")
                }
                else
                {
                   TextView.setText("Ainda não há dados neste sensor")
                }

                TextView.setText(sb)
            }
        }
        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

    }
}