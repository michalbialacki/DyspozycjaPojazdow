package com.example.test.Fragmenty.Admin

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_show_departures.*
import kotlinx.android.synthetic.main.fragment_wybor_pojazdu.*
import kotlin.math.log


class ShowDepartures : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_departures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = FirebaseDatabase.getInstance()
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        lateinit var vehiclesIDDropdown : MutableList<String>
        viewModel.adminVehicleList.forEach {
            vehiclesIDDropdown.add(it.vehicleID)
        }

            val adminVehicleSpinner = view.findViewById<Spinner>(R.id.spn_DeparturesSpin)
            val vehicleAdapter = ArrayAdapter<String>(requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                vehiclesIDDropdown)
            adminVehicleSpinner.adapter = vehicleAdapter
            chooseVehicle()
        }






    private fun chooseVehicle() {
        spn_DeparturesSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val database = FirebaseDatabase.getInstance()
                var ordersReference = database.getReference("Rozkazy wyjazdu")
                var vehicleID = parent?.getItemAtPosition(position).toString()
                lateinit var orderTable : MutableList <String>
                orderTable.clear()

                //FIREBASE GET dates
                getOrderDate(ordersReference,vehicleID,orderTable)


            }




            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun chooseDate() {
        spn_ChooseDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var dateSelected = parent?.getItemAtPosition(position).toString()
                val database = FirebaseDatabase.getInstance()
                var ordersReference = database.getReference("Rozkazy wyjazdu")
                ordersReference.child(dateSelected).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val departureDetails = snapshot.value
                            .toString()
                        departureDetails.split(';')
                            .toMutableList()
                        Log.e(TAG, ": ${departureDetails[0]}" )
                        Log.e(TAG, ": ${departureDetails[1]}" )
                        Log.e(TAG, ": ${departureDetails[2]}" )
                        Log.e(TAG, ": ${departureDetails[3]}")
                        TODO("dokoncza")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Data Cancel")
                    }
                })

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun getOrderDate(orderReference: DatabaseReference,vehicleID : String, orderTable : MutableList<String> ) {
        orderReference.child(vehicleID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var lista = snapshot.key.toString()
                lista.split(";")
                    .toMutableList<String>()
                lista.forEach {
                    orderTable.add(it.toString())
                }
                val dateSpinner = view!!.findViewById<Spinner>(R.id.spn_ChooseDate)
                val dateAdapter = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    orderTable
                )
                dateSpinner?.adapter = dateAdapter
                view!!.findViewById<Spinner>(R.id.spn_ChooseDate).visibility = View.VISIBLE
                chooseDate()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: Bledna sciezka lub brak danych ", )
            }
        })
    }

}