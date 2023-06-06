package com.example.expertfinder.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.expertfinder.R

// Reg NO :2200482
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
    }

    fun ClickRegisterWorker(view: View) {

        if(!isInternetAvailable(this)){
            Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this@RegisterActivity, RegisterWorkerActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun ClickRegisterCustomer(view: View) {

        if(!isInternetAvailable(this)){
            Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this@RegisterActivity, RegisterCustomerActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
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