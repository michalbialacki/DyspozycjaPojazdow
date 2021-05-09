package com.example.test.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.R
import kotlinx.android.synthetic.main.driver_cardview.view.*

class SelectedDriverAdapter (private val vehicleIDs: MutableList<String>) : RecyclerView.Adapter<SelectedDriverAdapter.ViewHolder> (){
    class ViewHolder (cardView: CardView) : RecyclerView.ViewHolder(cardView){
        val vehicleID : TextView = cardView.tv_VehicleID

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.driver_cardview, parent, false) as CardView



        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = vehicleIDs[position]

        holder.vehicleID.text = currentItem
    }
    override fun getItemCount() = vehicleIDs.size


}
