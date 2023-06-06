package com.example.expertfinder.activities
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expertfinder.R
import android.view.ViewGroup
import android.view.View
import android.widget.Toast
import com.example.expertfinder.adapters.OrderAdapter
import com.example.expertfinder.models.OrderModel
import com.google.firebase.database.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// Reg NO :2200482
class BookedWorkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var orderRecylerView: RecyclerView
        lateinit var workLodaingName: TextView
        lateinit var orderList : ArrayList<OrderModel>
        lateinit var dbRef: DatabaseReference



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_worker)

        orderRecylerView = findViewById(R.id.recycleOrder)
        orderRecylerView.layoutManager = LinearLayoutManager(this)
        orderRecylerView.setHasFixedSize(true)
        workLodaingName = findViewById(R.id.recyleName1)


        orderList = arrayListOf<OrderModel>()


        orderRecylerView.visibility = View.GONE
        workLodaingName.visibility = View.VISIBLE


        dbRef = FirebaseDatabase.getInstance().getReference("Order")
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()
                if (snapshot.exists()){
                    for (orderSnap in snapshot.children){
                        val orderData = orderSnap.getValue(OrderModel::class.java)
                        if(orderData?.CustomerName.equals(getLoggedInUserName()))
                            orderList.add(orderData!!)
                    }
                    val mAdapter = OrderAdapter(orderList)
                    mAdapter.setOnItemClickListener(object : OrderAdapter.onItemClickListener{

                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@BookedWorkerActivity, OrderDetailActivity::class.java)

                            //put extras
                            intent.putExtra("OrderId", orderList[position].OrderId)
                            intent.putExtra("WorkerName", orderList[position].WorkerName)
                            intent.putExtra("WorkerEmail", orderList[position].WorkerEmail)
                            intent.putExtra("WorkerLocation", orderList[position].WorkerLocation)
                            intent.putExtra("CustomerName", orderList[position].CustomerName)
                            startActivity(intent)
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
    override fun onBackPressed() {
        val intent = Intent(this@BookedWorkerActivity, CustomerHomeActivity::class.java)
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

    private fun getLoggedInUserName(): String? {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_NAME", "")
    }
}