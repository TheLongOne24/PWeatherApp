package com.example.pweatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pweatherapp.R

class DummyAdapter : RecyclerView.Adapter<DummyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.tv_Item_time)
        val temp: TextView = view.findViewById(R.id.tv_item_temp)
        val status: TextView = view.findViewById(R.id.tv_item_status)
        val image: ImageView = view.findViewById(R.id.img_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 5  // just 5 fake items

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.time.text = "Day ${position + 1}"
        holder.temp.text = "${20 + position}°C"
        holder.status.text = "Clear"

        // Use your local drawable (you already have one)
        holder.image.setImageResource(R.drawable.humidity)
    }
}