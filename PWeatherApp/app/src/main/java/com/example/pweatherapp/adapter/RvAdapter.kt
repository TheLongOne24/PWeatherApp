package com.example.pweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pweatherapp.databinding.RvItemLayoutBinding
import com.example.pweatherapp.extra.Switcher

class RvAdapter(
    private val forecastList: ArrayList<Array<String>>
) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    class ViewHolder(val binding: RvItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = forecastList[position]

        val temp = item[0]
        val description = item[1]
        val time = item[2]
        val icon = item[3]

        holder.binding.apply {

            imgItem.setImageResource(Switcher(icon))
            tvItemTemp.text = "$temp °C"
            tvItemStatus.text = description
            tvItemTime.text = time
        }
    }

    override fun getItemCount(): Int = forecastList.size
}