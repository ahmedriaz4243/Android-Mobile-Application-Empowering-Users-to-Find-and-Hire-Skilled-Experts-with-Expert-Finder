package com.example.expertfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expertfinder.R
import com.example.expertfinder.models.WorkerModel

// Reg NO :2200482
class WorkerAdapter (private val workerlist:ArrayList<WorkerModel>):
    RecyclerView.Adapter<WorkerAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener : onItemClickListener){
        mListener = clickListener;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.worker_list_item, parent, false)
        return ViewHolder(itemview, mListener)
    }

    override fun onBindViewHolder(holder: WorkerAdapter.ViewHolder, position: Int){
        val CurrWorker = workerlist[position]
        holder.workerName.text = CurrWorker.WorkerName
        holder.workerLocation.text = CurrWorker.WorkerLocation
        println(holder.workerName.text)
        println(holder.workerLocation.text)
        println("===========")
    }

    override fun getItemCount(): Int {
        return workerlist.size
    }

    class ViewHolder(itemView:View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val workerName: TextView = itemView.findViewById(R.id.listName)
        val workerLocation: TextView = itemView.findViewById((R.id.listLocation))

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}