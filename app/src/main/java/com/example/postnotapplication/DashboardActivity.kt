package com.example.postnotapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class DashboardActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        var database = FirebaseDatabase.getInstance().reference
        //database.setValue("entrei")
        auth = FirebaseAuth.getInstance()
        createNotificationChannel()     // chama a funcao createNOtificationChannel


        var getdata = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                for(i in snapshot.children){
                    var state = i.child("caixadecorreio").getValue()
                    sb.append("Estado: $state")
                    if(state == "Aberta")
                    {
                        Toast.makeText(
                            baseContext,
                            "CORREIO FOI ABERTO",
                            Toast.LENGTH_SHORT
                        ).show()

                        sendNotification()
                    }
                }
                state.setText(sb)

            }
        }
        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)


    }

    private fun sendNotification() {

        // imagem
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.caixacorreio)

        // send notification
        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Caixa de Correio")
            .setContentText("ABERTA")
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
                auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.historico -> {
                val intent = Intent(this, HistoricoDados::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}