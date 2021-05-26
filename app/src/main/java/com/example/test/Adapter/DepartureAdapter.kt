package com.example.test.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.R
import kotlinx.android.synthetic.main.departure_cardview.view.*
import kotlinx.android.synthetic.main.driver_cardview.view.*

class DepartureAdapter(private val departureData : MutableList<DeparturesDataClass>, val positionListener : AdapterPositionInterface ) : RecyclerView.Adapter<DepartureAdapter.ViewHolder> (){
    class ViewHolder (cardView: CardView) : RecyclerView.ViewHolder(cardView){
        val vehicleID : TextView = cardView.tv_depVehicle
        val vehicleDriver : TextView = cardView.tv_depName
        val depPurple : TextView = cardView.tv_depPurp
        val depType : TextView = cardView.tv_depType
        val depRoute : TextView = cardView.tv_depRoute
        val depDisposer : TextView = cardView.tv_depDisposer

        fun gettingAdapterPositionOut(positionListener: AdapterPositionInterface){
            itemView.setOnClickListener {
                positionListener.onDriverClicked(position)
            }
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartureAdapter.ViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.departure_cardview, parent, false) as CardView

        return DepartureAdapter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: DepartureAdapter.ViewHolder, position: Int) {
        val currentItem = departureData[position]

        holder.vehicleID.text = currentItem.vehicleID
        holder.vehicleDriver.text = currentItem.driverName
        holder.depPurple.text = currentItem.departurePurpose
        holder.depType.text = currentItem.departureType
        holder.depRoute.text = currentItem.departureRoute
        holder.depDisposer.text = "Drugi dyspoytor: " + currentItem.departureDisposer
        holder.gettingAdapterPositionOut(positionListener)

    }

    override fun getItemCount() = departureData.size

}