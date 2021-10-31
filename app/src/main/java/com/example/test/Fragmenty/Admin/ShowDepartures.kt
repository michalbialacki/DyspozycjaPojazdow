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
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_show_departures.*
import kotlinx.android.synthetic.main.fragment_wybor_pojazdu.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.log
import kotlin.reflect.typeOf


class ShowDepartures : Fragment() {
    var testSet = 31



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
        var vehiclesIDDropdown = mutableListOf<String>()
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
                val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
                var ordersReference = database.getReference("Rozkazy wyjazdu")
                var vehicleID = parent?.getItemAtPosition(position).toString()
                lt_OrderDetails.visibility = View.INVISIBLE
                viewModel.adminVehicleList.forEachIndexed({ index, element ->
                    if (element.vehicleID.equals(vehicleID)) {
                        testSet = index
                    }
                })
                
                var orderTable = mutableListOf<String>()

                orderTable.clear()
                btn_ShowRoute.setOnClickListener {
                    Navigation.findNavController(requireView()).navigate(R.id.vehicleSelected)
                }

                //FIREBASE GET dates
                getOrderDate(ordersReference,vehicleID,orderTable)


            }




            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun chooseDate(vehicleID: String) {
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
                val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
                viewModel.latTable.clear()
                viewModel.lngTable.clear()
                if (dateSelected.isDigitsOnly()){
                    lt_OrderDetails.visibility = View.VISIBLE
                    ordersReference.child(vehicleID).child(dateSelected).addListenerForSingleValueEvent(
                        object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val departureDetails =
                                    snapshot.value.toString().removeSurrounding("{", "}").split(",")
                                        .toMutableList()
                                tv_departurePurpose.text =
                                    departureDetails[0].removePrefix("Cel wyjazdu=")
                                tv_DepartureType.text =
                                    departureDetails[4].removePrefix(" Rodzaj przewozu=")
                                tv_Route.text = departureDetails[2].removePrefix(" Kurs=")
                                tv_Driver.text = departureDetails[3].removePrefix(" Kierowca=")
                                tv_Dispo.text =
                                    departureDetails[1].removePrefix(" Drugi dysponent=")
                                if (departureDetails.size > 5) {
                                    var checkpointTable = mutableListOf<String>()
                                    snapshot.child("Rozliczenie Pojazdu").children.forEach {
                                        checkpointTable.add(it.child("driverCoordinates").value.toString())
                                    }

                                    checkpointTable.forEach {
                                        var lista = it.split(" , ")
                                            .toMutableList()
                                        viewModel.latTable.add(lista[0].toDouble())
                                        viewModel.lngTable.add(lista[1].toDouble())
                                    }

                                } else {
                                    Log.e(TAG, "Brak rozliczenia pojazdu",)
                                }


                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(TAG, "Data Cancel")
                            }
                        })
                }
                else{
                    Log.e(TAG, "onItemSelected: Dana w spinnerze to nie liczba", )
                }
                val thisDay = SimpleDateFormat("ddMMYYYYHHMMM").format(Date())
                val dayValues = mutableListOf<String>(thisDay.slice(0..1),thisDay.slice(2..3),thisDay.slice(4..8))
                val dateOfOrder = SimpleDateFormat("ddMMYYYYHHMM").parse(dateSelected)
                if (dateOfOrder.before(SimpleDateFormat("ddMMYYYYHHMM").parse(viewModel.DayForUser))){
                    btn_ShowRoute.text = "Rozliczenie pojazdu"
                    btn_ShowRoute.setOnClickListener {
                        Navigation.findNavController(requireView()).navigate(R.id.checkpointTable)
                        viewModel.vehicleAdapterPosition.value = testSet
                    }
                }
                else{

                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }

    private fun getOrderDate(orderReference: DatabaseReference,vehicleID : String, orderTable : MutableList<String> ) {
        orderReference.child(vehicleID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var lista = snapshot.children.forEach{
                    orderTable.add(it.key.toString())
                }
                val dateSpinner = view!!.findViewById<Spinner>(R.id.spn_ChooseDate)
                val dateAdapter = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    orderTable
                )
                dateSpinner?.adapter = dateAdapter
                view!!.findViewById<Spinner>(R.id.spn_ChooseDate).visibility = View.VISIBLE
                chooseDate(vehicleID)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: Bledna sciezka lub brak danych ", )
            }
        })
    }

}