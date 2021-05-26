package com.example.test.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import kotlinx.android.synthetic.main.cardview.view.*

class SysDyspAdapter (private val listaPunktowTrasy : MutableList<DaneRecycler> ) : RecyclerView.Adapter<SysDyspAdapter.ViewHolder> () {
    class ViewHolder (cardView: CardView) : RecyclerView.ViewHolder(cardView){
        val punktTrasy : TextView = cardView.tv_depVehicle
        val stanLicznika : TextView = cardView.tv_depPurp
        val iloscKilometrow : TextView = cardView.tv_depName

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.cardview, parent, false) as CardView


        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listaPunktowTrasy[position]

        holder.punktTrasy.text = currentItem.punktTrasyTablica
        holder.stanLicznika.text = currentItem.stanLicznikaTablica
        holder.iloscKilometrow.text = currentItem.iloscKilometrowTablica

    }


    //ArrayList<DaneRecycler>
    override fun getItemCount() = listaPunktowTrasy.size


}