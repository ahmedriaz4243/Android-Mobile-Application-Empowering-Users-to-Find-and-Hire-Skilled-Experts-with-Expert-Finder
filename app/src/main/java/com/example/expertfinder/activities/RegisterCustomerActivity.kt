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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.expertfinder.R
import com.example.expertfinder.models.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// Reg NO :2200482
class RegisterCustomerActivity : AppCompatActivity() {

    lateinit var getCustomername : EditText
    lateinit var getCustomeremail : EditText
    lateinit var getCustomerpassowrd : EditText
    lateinit var getCustomerlocation : EditText

    lateinit var btnregister : Button

    lateinit var dbref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_customer)

        getCustomername = findViewById(R.id.username)
        getCustomeremail = findViewById(R.id.useremail)
        getCustomerpassowrd = findViewById(R.id.userpassword)
        getCustomerlocation = findViewById(R.id.userlocation)
        btnregister = findViewById(R.id.btnuserreg)

        dbref = FirebaseDatabase.getInstance().getReference("User")

        btnregister.setOnClickListener{
            if(!isInternetAvailable(this)){
                Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            }
            else{
                saveUserData()
            }
        }

    }

    fun saveUserData() {
        val username = getCustomername.text.toString()
        val useremail = getCustomeremail.text.toString()
        val userpassword = getCustomerpassowrd.text.toString()
        val userlocation = getCustomerlocation.text.toString()

        var isadd: Boolean = true

        if(username.isEmpty()) {
            getCustomername.error = "Please enter the name"
            isadd = false
        }
        if(useremail.isEmpty()) {
            getCustomeremail.error = "Please enter the email"
            isadd = false
        }
        if(userpassword.isEmpty()) {
            getCustomerpassowrd.error = "Please enter the password"
            isadd = false
        }
        if(userlocation.isEmpty()) {
            getCustomerlocation.error = "Please enter the location"
            isadd = false
        }

        if(isadd == true) {
            val UserID = dbref.push().key!!
            val UserType = "User"

            var User =
                CustomerModel(UserID, username, useremail, userpassword, userlocation, UserType)
            dbref.child(UserID).setValue(User).addOnCompleteListener {
                getCustomername.text.clear()
                getCustomeremail.text.clear()
                getCustomerpassowrd.text.clear()
                getCustomerlocation.text.clear()

                val dialog = AlertDialog.Builder(this)

                dialog.setTitle("Customer Registerd")
                    .setMessage("Customer Register Successfully")
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


    fun RClickLogin(view: View) {
        if(!isInternetAvailable(this)){
            Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this@RegisterCustomerActivity, LoginActivity::class.java)
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