package com.example.expertfinder.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.expertfinder.R
import com.example.expertfinder.models.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// Reg NO :2200482
class RegisterWorkerActivity : AppCompatActivity() {

    lateinit var getWorkername : EditText
    lateinit var getWorkeremail : EditText
    lateinit var getWorkerpassowrd : EditText
    lateinit var getWorkerlocation : EditText
    lateinit var getWorkerskill : Spinner

    lateinit var btnWorkerlogin : Button
    lateinit var btnWorkerregister : Button

    lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_worker)

        getWorkername = findViewById(R.id.userWorkername)
        getWorkeremail = findViewById(R.id.Workeremail)
        getWorkerpassowrd = findViewById(R.id.Workerpassword)
        getWorkerlocation = findViewById(R.id.Workerlocation)
        getWorkerskill = findViewById(R.id.Workerskill)
        btnWorkerregister = findViewById(R.id.btnWorkerreg)

        dbref = FirebaseDatabase.getInstance().getReference("Worker")
        btnWorkerregister.setOnClickListener{
            if(!isInternetAvailable(this)){
                Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            }
            else {
                saveWorkerData()
            }
        }
    }
    fun saveWorkerData() {
        val username = getWorkername.text.toString()
        val useremail = getWorkeremail.text.toString()
        val userpassword = getWorkerpassowrd.text.toString()
        val userlocation = getWorkerlocation.text.toString()
        val userSkill = getWorkerskill.selectedItem.toString()
        var isadd: Boolean = true

        if(username.isEmpty()) {
            getWorkername.error = "Please enter the name"
            isadd = false
        }
        if(useremail.isEmpty()) {
            getWorkeremail.error = "Please enter the email"
            isadd = false
        }
        if(userpassword.isEmpty()) {
            getWorkerpassowrd.error = "Please enter the password"
            isadd = false
        }
        if(userlocation.isEmpty()) {
            getWorkerlocation.error = "Please enter the location"
            isadd = false
        }

        if(isadd == true) {
            val WorkerID = dbref.push().key!!
            val WorkerType = "Worker"

            var Worker = WorkerModel(WorkerID, username, useremail, userpassword, userlocation, userSkill, WorkerType)
            dbref.child(WorkerID).setValue(Worker).addOnCompleteListener {
                getWorkername.text.clear()
                getWorkeremail.text.clear()
                getWorkerpassowrd.text.clear()
                getWorkerlocation.text.clear()
                val dialog = AlertDialog.Builder(this)

                dialog.setTitle("Worker Registerd")
                    .setMessage("Worker Register Successfully")
                    .setPositiveButton("OK") { dialog, whichButton ->

                    }

                dialog.show()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Not Register Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    fun RTClickLogin(view: View) {
        if(!isInternetAvailable(this)){
            Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this@RegisterWorkerActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
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
}