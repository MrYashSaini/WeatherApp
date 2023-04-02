package com.charge.ysweatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(private val list: List<model>): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
        val time : TextView = view.findViewById(R.id.tvSampleTime)
        val temp: TextView = view.findViewById(R.id.tvSampleTemp)
        val status: TextView = view.findViewById(R.id.tvSampleCondition)
        val humidity: TextView = view.findViewById(R.id.tvSampleHumidity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_sample,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storeTime = list.get(position).time.subSequence(1,3).toString().toInt()
        if(storeTime>12){
            holder.time.text = (storeTime-12).toString()+":00 PM"
        }
        else if (storeTime==12){
            holder.time.text = "12:00 PM"
        }
        else{
            holder.time.text = list.get(position).time+" AM"
        }
        holder.temp.text = list.get(position).temp
        holder.humidity.text = list.get(position).humidity+"%"
        holder.status.text = list.get(position).condition

    }
}