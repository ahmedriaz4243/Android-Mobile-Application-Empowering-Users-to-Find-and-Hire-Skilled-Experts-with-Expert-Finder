package com.example.expertfinder.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.view.Menu
import android.view.MenuItem
import com.example.expertfinder.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expertfinder.adapters.WorkerAdapter
import com.example.expertfinder.models.WorkerModel
import com.google.firebase.database.*
import android.widget.Toast

// Reg NO :2200482
class AvailableWorkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var workerRecylerView: RecyclerView
        lateinit var workLodaingName: TextView
        lateinit var workerList : ArrayList<WorkerModel>
        lateinit var dbRef: DatabaseReference

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_worker)

        workerRecylerView = findViewById(R.id.recycleWorker)
        workerRecylerView.layoutManager = LinearLayoutManager(this)
        workerRecylerView.setHasFixedSize(true)
        workLodaingName = findViewById(R.id.recyleName)


        workerList = arrayListOf<WorkerModel>()

        workerRecylerView.visibility = View.GONE
        workLodaingName.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Worker")
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                workerList.clear()
                if (snapshot.exists()){
                    for (workSnap in snapshot.children){
                        val workData = workSnap.getValue(WorkerModel::class.java)
                        if(workData?.WorkerSkill?.uppercase()?.trim().equals(intent.getStringExtra("SKILL")))
                            workerList.add(workData!!)
                    }
                    val mAdapter = WorkerAdapter(workerList)
                    workerRecylerView.adapter = mAdapter


                    mAdapter.setOnItemClickListener(object : WorkerAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@AvailableWorkerActivity, WorkerDetailActivity::class.java)

                            //put extras
                            intent.putExtra("WorkerId", workerList[position].WorkerId)
                            intent.putExtra("WorkerName", workerList[position].WorkerName)
                            intent.putExtra("WorkerEmail", workerList[position].WorkerEmail)
                            intent.putExtra("WorkerLocation", workerList[position].WorkerLocation)
                            intent.putExtra("WorkerSkill", workerList[position].WorkerSkill)
                            startActivity(intent)

                        }

                    })

                    workerRecylerView.visibility = View.VISIBLE
                    workLodaingName.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onBackPressed() {
        val intent = Intent(this@AvailableWorkerActivity, CustomerHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.service -> {
                Toast.makeText(applicationContext, "Available Services", Toast.LENGTH_LONG).show()
                val intent = Intent(this@AvailableWorkerActivity, CustomerHomeActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.order -> {
                Toast.makeText(applicationContext, "Booked Workers", Toast.LENGTH_LONG).show()
                val intent = Intent(this@AvailableWorkerActivity, BookedWorkerActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.exit -> {
                Toast.makeText(applicationContext, "Logout", Toast.LENGTH_LONG).show()
                val intent = Intent(this@AvailableWorkerActivity, LoginActivity::class.java)
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