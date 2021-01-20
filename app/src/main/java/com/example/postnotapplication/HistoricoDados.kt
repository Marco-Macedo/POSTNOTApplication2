package com.example.postnotapplication

import android.content.Context
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
    private lateinit var nomesharedpreference : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var database = FirebaseDatabase.getInstance().reference
        setContentView(R.layout.activity_historico_dados)
        auth = FirebaseAuth.getInstance()

        var token = getSharedPreferences("key", Context.MODE_PRIVATE)
        nomesharedpreference = token.getString("caixadecorreio"," ").toString()


        var getdata = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()

                    var Janeiro = snapshot.child(nomesharedpreference + "/2021/janeiro").getValue() // historico de dados
                    //var Fevereiro = snapshot.child(nomesharedpreference + "/2021/fevereiro").getValue() // historico de dados
                    // var Março = snapshot.child(nomesharedpreference + "/2021/março").getValue() // historico de dados
                    if (Janeiro != null) {
                        sb.append("$Janeiro")
                        TextView.setText(sb)
                    }
                    else
                    {
                        TextView.setText("Caixa de correio sem dados.")
                    }
            }
        }
        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

    }
}