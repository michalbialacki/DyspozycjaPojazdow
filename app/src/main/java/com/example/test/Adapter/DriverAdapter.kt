package com.example.test.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.R
import kotlinx.android.synthetic.main.driver_cardview.view.*


class DriverAdapter (private val driverData : MutableList<DriverDataClass>, val positionListener : AdapterPositionInterface) : RecyclerView.Adapter<DriverAdapter.ViewHolder> (){
    class ViewHolder (cardView: CardView) : RecyclerView.ViewHolder(cardView){
        val driverName : TextView = cardView.tv_VehicleID
        val driverID : TextView = cardView.tv_vehicleType
        val driverStatus : TextView = cardView.tv_VehicleStatus

        fun gettingAdapterPositionOut(positionListener: AdapterPositionInterface){
            itemView.setOnClickListener{
                positionListener.onDriverClicked(adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.driver_cardview, parent, false) as CardView



        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = driverData[position]

        holder.driverName.text = currentItem.driverFullName
        holder.driverID.text = currentItem.driverID
        holder.driverStatus.text = currentItem.driverStatus
        holder.gettingAdapterPositionOut(positionListener)




    }


    //ArrayList<DaneRecycler>
    override fun getItemCount() = driverData.size


}
