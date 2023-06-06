package com.example.expertfinder.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.expertfinder.R
import com.example.expertfinder.models.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// Reg NO :2200482
class WorkerDetailActivity : AppCompatActivity() {

    private lateinit var WorkerName: TextView
    private lateinit var WorkerEmail: TextView
    private lateinit var WorkerLocation: TextView
    private lateinit var WorkerSkill: TextView
    private lateinit var btnWorkerEmail : Button
    private lateinit var btnWorkerBook : Button
    lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_detail)
        btnWorkerBook = findViewById(R.id.btnWorkBook)
        btnWorkerEmail = findViewById(R.id.btnWorkEmail)

        initValues()
        setDetailValuesToWorker()

        btnWorkerBook.setOnClickListener{
            ClickBookAction()
        }

        btnWorkerEmail.setOnClickListener{
            ClickEmailAction()
        }
    }

    fun ClickEmailAction(){

        if(!isInternetAvailable(this)){
            Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this,"Email to Worker!", Toast.LENGTH_SHORT).show()

        WorkerEmail = findViewById(R.id.WorkerEmail)
        if((WorkerEmail.text.toString()).isEmpty()) {
            WorkerEmail.text = "abc@gmail.com"
        }

        println(WorkerEmail.text.toString())


        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "plain/text"
        intent.putExtra(Intent.EXTRA_EMAIL, WorkerEmail.text.toString())
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query to Worker")

        intent.putExtra(
            Intent.EXTRA_TEXT, """ Hello! I need a Worker for my Task""".trimIndent()
        )
        startActivity(Intent.createChooser(intent, ""))
    }

    private fun ClickBookAction () {

        if(!isInternetAvailable(this)){
            Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            return
        }

        dbref = FirebaseDatabase.getInstance().getReference("Order")

        val OrderCustomer = getLoggedInUserName()
        val OrderWorker = WorkerName.text.toString()
        val OrderEmail = WorkerEmail.text.toString()
        val userLocation = WorkerLocation.text.toString()

        val OrderID = dbref.push().key!!

        var Order = OrderModel(OrderID, OrderCustomer, OrderWorker, OrderEmail,userLocation)
        dbref.child(OrderID).setValue(Order).addOnCompleteListener{

            /////

            val dialog = AlertDialog.Builder(this)

            dialog.setTitle("Worker Booked")
                .setMessage("Your Worker Booked Successfully")
                .setPositiveButton("OK") { dialog, whichButton ->

                }

            dialog.show()
            /////

            addNotification()
        }.addOnFailureListener{
                err->
            Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initValues() {


        WorkerName = findViewById(R.id.WorkerName)
        if((WorkerName.text.toString()).isEmpty()) {
            WorkerName.text = "Worker Name"
        }
        WorkerLocation = findViewById(R.id.WorkerLocation)
        if((WorkerLocation.text.toString()).isEmpty()) {
            WorkerLocation.text = "Worker Location"
        }
        WorkerEmail = findViewById(R.id.WorkerEmail)
        if((WorkerEmail.text.toString()).isEmpty()) {
            WorkerEmail.text = "Worker Email"
        }
        WorkerSkill = findViewById(R.id.WorkerSkill)
        if((WorkerSkill.text.toString()).isEmpty()) {
            WorkerSkill.text = "Worker Skill"
        }
    }

    private fun setDetailValuesToWorker(){
        WorkerName.text = intent.getStringExtra("WorkerName")
        WorkerEmail.text = intent.getStringExtra("WorkerEmail")
        WorkerLocation.text = intent.getStringExtra("WorkerLocation")
        WorkerSkill.text = intent.getStringExtra("WorkerSkill")
    }

    fun isInternetAvailable(context: Context) : Boolean{
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        if(connectivityManager!=null){
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if(networkCapabilities != null){
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    return true
                }
            }
        }

        return false
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }


    private fun getLoggedInUserName(): String? {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_NAME", "")
    }

    private fun addNotification() {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ef)
            .setContentTitle("Booking Confirmed!")
            .setContentText("Your Worker Booked Successfully")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Notifications ",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }

        manager.notify(0, builder.build())
    }
}