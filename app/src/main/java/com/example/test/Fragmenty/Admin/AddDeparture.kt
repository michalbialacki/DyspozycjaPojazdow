package com.example.test.Fragmenty.Admin

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_departure.*
import kotlinx.android.synthetic.main.fragment_startowy.*
import java.text.SimpleDateFormat
import java.util.*


class AddDeparture : Fragment(), DatePickerDialog.OnDateSetListener, BackPressed {
    private lateinit var viewModel: ViewModelSystemuDyspozycji
    private var database = FirebaseDatabase.getInstance()
    private var departuresRef = database.getReference("Wyjazdy")
    private var datePath = ""
    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var savedDate = ""
    private var departureType =""
    private var departurePurpose = ""
    private lateinit var checkDate : SimpleDateFormat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)

        val driverName = viewModel.driverNameList[viewModel.driverAdapterPosition.value!!]
        val vehicleSelected = viewModel.vehicleList[viewModel.vehicleAdapterPosition.value!!]
        getDepPurpose()
        getDepType()
        pickDate()
        btn_SaveDep.visibility = View.INVISIBLE
        btn_SaveDep.setOnClickListener{
            var route = edt_SetRoute.text.toString()
            var depDisposer = edt_depDisposer.text.toString()
            departuresRef.child(savedDate).child(driverName).child(vehicleSelected).child("Trasa").setValue(route)
            departuresRef.child(savedDate).child(driverName).child(vehicleSelected).child("Cel wyjazdu").setValue(departurePurpose)
            departuresRef.child(savedDate).child(driverName).child(vehicleSelected).child("Rodzaj wyjazdu").setValue(departureType)
            departuresRef.child(savedDate).child(driverName).child(vehicleSelected).child("Drugi dysponent").setValue(depDisposer)
        }


    }

    private fun pickDate() {
        btn_datePick.setOnClickListener{
            getCalendarDate()
            DatePickerDialog(requireContext(),this,year,month,day).show()
        }

    }

    private fun getCalendarDate() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_departure, container, false)
    }

    companion object {

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month +1
        savedYear = year
        if(savedMonth<10)
        {
            savedDate = "${savedDay}"+"0"+"$savedMonth"+"$savedYear"
        }
        else{
            savedDate = "${savedDay}"+"$savedMonth"+"$savedYear"
        }


        var checkDate = SimpleDateFormat("ddMMyyyy")
        if (checkDate.parse(savedDate).before(Date(savedYear,savedMonth,savedDay)))
        {
            btn_SaveDep.visibility = View.VISIBLE

            Toast.makeText(requireContext(), "Wprowadzona data jest poprawna  $savedDate  ",Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(requireContext(), "Wprowadzona data już minęła!",Toast.LENGTH_SHORT).show()
        }
    }


    ////////////////////////////////////////////////FUNCKCJE////////////////////////////////////////

    private fun getDepPurpose() {
        spn_DeparturePurpose.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
                departurePurpose= parent?.getItemAtPosition(position).toString()
                viewModel.zapiszCelWyjazdu(departurePurpose)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Wybierz cel wyjazdu!", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun getDepType() {
        spn_DepartureType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
                departureType = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Wybierz rodzaj przewozu!", Toast.LENGTH_LONG)
                    .show()
                return
            }

        }
    }

    override fun backPressed() {
        TODO("Not yet implemented")
    }
}