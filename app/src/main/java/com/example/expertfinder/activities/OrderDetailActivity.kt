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
class OrderDetailActivity : AppCompatActivity() {

    private lateinit var WorkerName: TextView
    private lateinit var WorkerEmail: TextView
    private lateinit var WorkerLocation: TextView
    private lateinit var CustomerName: TextView
    private lateinit var btnWorkerEmail : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        btnWorkerEmail = findViewById(R.id.btnOWorkEmail)

        initValues()
        setDetailValuesToOrder()

        btnWorkerEmail.setOnClickListener{
            ClickEmailAction()
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
        CustomerName = findViewById(R.id.CustomerName)
        if((CustomerName.text.toString()).isEmpty()) {
            CustomerName.text = "Customer Name"
        }
    }

    private fun setDetailValuesToOrder(){
        WorkerName.text = intent.getStringExtra("WorkerName")
        WorkerEmail.text = intent.getStringExtra("WorkerEmail")
        WorkerLocation.text = intent.getStringExtra("WorkerLocation")
        CustomerName.text = intent.getStringExtra("CustomerName")
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
            Intent.EXTRA_TEXT, """ Hello! I have a query regarding my order""".trimIndent()
        )
        startActivity(Intent.createChooser(intent, ""))
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
}