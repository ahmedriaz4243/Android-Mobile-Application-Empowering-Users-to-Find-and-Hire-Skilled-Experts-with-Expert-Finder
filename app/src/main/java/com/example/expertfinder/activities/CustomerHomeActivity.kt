package com.example.expertfinder.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.MenuItem
import android.view.Menu
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expertfinder.R

// Reg NO :2200482
class CustomerHomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_home)

        var electricianBtn: ImageView
        electricianBtn = findViewById(R.id.imgelc);

        electricianBtn.setOnClickListener {
            val intent = Intent(this@CustomerHomeActivity, AvailableWorkerActivity::class.java)
            intent.putExtra("SKILL", "ELECTRICIAN")
            startActivity(intent)
            finish()
        }

        var plumberBtn: ImageView
        plumberBtn = findViewById(R.id.imgplu);
        plumberBtn.setOnClickListener {
            val intent = Intent(this@CustomerHomeActivity, AvailableWorkerActivity::class.java)
            intent.putExtra("SKILL", "PLUMBER")
            startActivity(intent)
            finish()
        }

        var carpenterBtn: ImageView
        carpenterBtn = findViewById(R.id.imgcar);
        carpenterBtn.setOnClickListener {
            val intent = Intent(this@CustomerHomeActivity, AvailableWorkerActivity::class.java)
            intent.putExtra("SKILL", "CARPENTER")
            startActivity(intent)
            finish()
        }

        var gasengineerBtn: ImageView
        gasengineerBtn = findViewById(R.id.imggas);
        gasengineerBtn.setOnClickListener {
            val intent = Intent(this@CustomerHomeActivity, AvailableWorkerActivity::class.java)
            intent.putExtra("SKILL", "GAS ENGINEER")
            startActivity(intent)
            finish()
        }

        var cleanerBtn: ImageView
        cleanerBtn = findViewById(R.id.imgcle);
        cleanerBtn.setOnClickListener {
            val intent = Intent(this@CustomerHomeActivity, AvailableWorkerActivity::class.java)
            intent.putExtra("SKILL", "CLEANER")
            startActivity(intent)
            finish()
        }

        var painterBtn: ImageView
        painterBtn = findViewById(R.id.imgpai);
        painterBtn.setOnClickListener {
            val intent = Intent(this@CustomerHomeActivity, AvailableWorkerActivity::class.java)
            intent.putExtra("SKILL", "PAINTER")
            startActivity(intent)
            finish()
        }

        var personalcareBtn: ImageView
        personalcareBtn = findViewById(R.id.imgper);
        personalcareBtn.setOnClickListener {
            val intent = Intent(this@CustomerHomeActivity, AvailableWorkerActivity::class.java)
            intent.putExtra("SKILL", "PERSONAL CARE")
            startActivity(intent)
            finish()
        }

        var builderBtn: ImageView
        builderBtn = findViewById(R.id.imgbui);
        builderBtn.setOnClickListener {
            val intent = Intent(this@CustomerHomeActivity, AvailableWorkerActivity::class.java)
            intent.putExtra("SKILL", "BUILDER")
            startActivity(intent)
            finish()
        }


    }

    private var pressedTime: Long = 0
    override fun onBackPressed() {
        if (pressedTime + 1800 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menumain, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {


            R.id.order -> {
                Toast.makeText(applicationContext, "Booked Workers", Toast.LENGTH_LONG).show()
                val intent = Intent(this@CustomerHomeActivity, BookedWorkerActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.exit -> {
                Toast.makeText(applicationContext, "Logout", Toast.LENGTH_LONG).show()
                val intent = Intent(this@CustomerHomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
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

