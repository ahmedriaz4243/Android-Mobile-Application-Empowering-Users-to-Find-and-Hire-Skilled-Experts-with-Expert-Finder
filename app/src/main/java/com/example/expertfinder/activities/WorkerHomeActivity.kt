package com.example.expertfinder.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expertfinder.R
import com.example.expertfinder.adapters.OrderAdapter
import com.example.expertfinder.models.OrderModel
import com.google.firebase.database.*
import androidx.appcompat.app.AlertDialog

// Reg NO :2200482
class WorkerHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var orderRecylerView: RecyclerView
        lateinit var workLodaingName: TextView
        lateinit var orderList : ArrayList<OrderModel>
        lateinit var dbRef: DatabaseReference


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_home)

        orderRecylerView = findViewById(R.id.recycleWOrder)
        orderRecylerView.layoutManager = LinearLayoutManager(this)
        orderRecylerView.setHasFixedSize(true)
        workLodaingName = findViewById(R.id.recyleName2)


        orderList = arrayListOf<OrderModel>()


        orderRecylerView.visibility = View.GONE
        workLodaingName.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Order")
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var CusName = ""
                orderList.clear()
                if (snapshot.exists()){
                    for (orderSnap in snapshot.children){
                        val orderData = orderSnap.getValue(OrderModel::class.java)
                        CusName = orderData?.CustomerName.toString()
                        if(orderData?.WorkerName.equals(getLoggedInUserName()))
                            orderList.add(orderData!!)
                    }
                    val mAdapter = OrderAdapter(orderList)
                    mAdapter.setOnItemClickListener(object : OrderAdapter.onItemClickListener{

                        override fun onItemClick(position: Int) {
                            val dialog = AlertDialog.Builder(this@WorkerHomeActivity)

                            dialog.setTitle("Customer Name")
                                .setMessage( CusName)
                                .setPositiveButton("OK") { dialog, whichButton ->

                                }

                            dialog.show()
                        }

                    })
                    orderRecylerView.adapter = mAdapter

                    orderRecylerView.visibility = View.VISIBLE
                    workLodaingName.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    var pressedTime: Long = 0
    override fun onBackPressed() {
        if (pressedTime + 1800 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menuworker, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.exit -> {
                Toast.makeText(applicationContext, "Logout", Toast.LENGTH_LONG).show()
                val intent = Intent(this@WorkerHomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
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