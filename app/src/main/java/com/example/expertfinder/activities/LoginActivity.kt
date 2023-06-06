package com.example.expertfinder.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expertfinder.models.*
import com.example.expertfinder.R
import com.google.firebase.database.*

// Reg NO :2200482
class LoginActivity : AppCompatActivity() {


    lateinit var getUserpassowrd : EditText
    lateinit var getUseremail : EditText


    lateinit var userDbRef : DatabaseReference
    lateinit var workerDbRef : DatabaseReference


    private var pressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun ClickLogin(view: View) {

        if(!isInternetAvailable(this)){
            Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            return
        }

        var customerName : String = ""

        getUseremail = findViewById(R.id.email)
        getUserpassowrd = findViewById(R.id.password)

        userDbRef = FirebaseDatabase.getInstance().getReference("User")
        userDbRef.get().addOnSuccessListener{
            if(it.exists()){
                var isValidCredentials : Boolean = false

                for (d in it.children) {
                    val data = d.getValue(CustomerModel::class.java)
                    println("Name -> " + data?.CustomerName)
                    if(data?.CustomerEmail?.equals(getUseremail.text.toString()) == true
                        && data?.CustomerPassword?.equals(getUserpassowrd.text.toString()) == true){
                        println("Valid Customer Credentials")
                        customerName = data.CustomerName.toString()
                        isValidCredentials = true
                        break
                    }
                }

                if(isValidCredentials){
                    // TODO Customer FLow

                    saveLoginDetails("CUSTOMER",customerName)

                    Toast.makeText(this,"Login Sucessfully",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, CustomerHomeActivity::class.java)

                    startActivity(intent)
                    finish()
                }else{
                    workerDbRef = FirebaseDatabase.getInstance().getReference("Worker")
                    workerDbRef.get().addOnSuccessListener{
                        for (d in it.children) {
                            val data = d.getValue(WorkerModel::class.java)
                            println("Worker Name -> " + data?.WorkerName)
                            if(data?.WorkerEmail?.equals(getUseremail.text.toString()) == true
                                && data?.WorkerPassword?.equals(getUserpassowrd.text.toString()) == true){
                                println("Valid Worker Credentials")
                                customerName = data.WorkerName.toString()
                                isValidCredentials = true
                                break
                            }
                        }
                        if(isValidCredentials){

                            Toast.makeText(this,"Login Sucessfully",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, WorkerHomeActivity::class.java)

                            saveLoginDetails("WORKER",customerName)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                        }

                    }.addOnFailureListener{
                        Toast.makeText(this,"Something went wrong ",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"User Not exist",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Something went wrong ",Toast.LENGTH_SHORT).show()
        }

    }

    fun ClickRegister(view: View) {

        if(!isInternetAvailable(this)){
            Toast.makeText(this,"Connect Your Internet",Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
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



    override fun onBackPressed() {
        if (pressedTime + 1800 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun saveLoginDetails(userType : String, userName : String){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply(){
            putString("USER_NAME", userName)
        }.apply()

        println("Data saved $userName, $userType")
    }
}