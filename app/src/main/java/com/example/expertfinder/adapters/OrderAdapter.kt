package com.example.expertfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expertfinder.R
import com.example.expertfinder.models.OrderModel

// Reg NO :2200482
class OrderAdapter (private val orderlist:ArrayList<OrderModel>):
    RecyclerView.Adapter<OrderAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener : onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return ViewHolder(itemview, mListener)
    }

    override fun onBindViewHolder(holder: OrderAdapter.ViewHolder, position: Int){
        val currWorker = orderlist[position]
        holder.workerName.text = currWorker.WorkerName
        holder.workerLocation.text = currWorker.WorkerLocation
        println(holder.workerName.text)
        println(holder.workerLocation.text)
        println("===========")
    }

    override fun getItemCount(): Int {
        return orderlist.size
    }

    class ViewHolder(itemView:View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val workerName: TextView = itemView.findViewById(R.id.listName1)
        val workerLocation: TextView = itemView.findViewById((R.id.listLocation1))

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}