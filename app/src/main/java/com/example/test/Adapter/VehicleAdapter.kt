package com.example.test.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.R
import kotlinx.android.synthetic.main.driver_cardview.view.*


class VehicleAdapter (private val vehicleData : MutableList<adminVehicleDataClass>, val positionListener : AdapterPositionInterface) : RecyclerView.Adapter<VehicleAdapter.ViewHolder> (){
    class ViewHolder (cardView: CardView) : RecyclerView.ViewHolder(cardView){
        val vehicleID : TextView = cardView.tv_VehicleID
        val vehicleStatus : TextView = cardView.tv_vehicleType

        fun gettingAdapterPositionOut(positionListener: AdapterPositionInterface){
            itemView.setOnClickListener{
                positionListener.onItemClick(adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.driver_cardview, parent, false) as CardView



        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = vehicleData[position]

        holder.vehicleID.text = currentItem.vehicleID
        holder.vehicleStatus.text = currentItem.vehicleStatus
        holder.gettingAdapterPositionOut(positionListener)




    }


    //ArrayList<DaneRecycler>
    override fun getItemCount() = vehicleData.size


}