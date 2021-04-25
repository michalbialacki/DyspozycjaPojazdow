package com.example.test.Fragmenty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_wybor_pojazdu.*
import timber.log.Timber
import timber.log.Timber.Tree


class WyborPojazdu : Fragment() {
    private lateinit var viewModel : ViewModelSystemuDyspozycji
    private var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wybor_pojazdu, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = FirebaseDatabase.getInstance()
        var vehiclesReference = database.getReference("Pojazdy")
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        val kotan = viewModel.przypisanePojazdydoKierowcy.value.toString()
                .split(", ")
                .toMutableList()
        kotan.removeAt(0)
        val vehicleSpinner = view.findViewById<Spinner>(R.id.spn_WyborPojazdu)
        val vehicleAdapter = ArrayAdapter<String>(requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                kotan)
        vehicleSpinner.adapter = vehicleAdapter
        vehicleChoice(vehiclesReference)
        changeStatus()

        btn_RozpocznijWyjazd.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_wyborPojazdu_to_startowy)
        }
    }


    private fun vehicleChoice(pojazdyRef: DatabaseReference) {
        spn_WyborPojazdu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var vehicleID = parent?.getItemAtPosition(position).toString()
                viewModel.RejestracjaPojazdu.
                postValue(vehicleID)

                overriteVehicleOdometer(pojazdyRef,vehicleID)

                Toast.makeText(
                    requireContext(),
                    "Zapisano pojazd: $vehicleID",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(),
                        "Wybierz cel wyjazdu!",
                        Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    private fun overriteVehicleOdometer(vehicleReference: DatabaseReference, vehicleID : String) {
        vehicleReference.child(vehicleID)
                .child("Stan licznika").
                addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val odometerValue = snapshot.getValue().toString().toDouble()
                viewModel.PoczatkowyStanLicznika
                        .postValue(odometerValue)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun changeStatus(){
        spn_StatusUzytkownika.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var driversStatus = parent?.getItemAtPosition(position).toString()
                if (driversStatus.equals("Wolne")){
                    Toast.makeText(requireContext(),
                            "Dziękujemy za Twoją pracę",
                            Toast.LENGTH_LONG).show()
                    viewModel.statusKierowcy
                            .postValue(driversStatus)
                    btn_RozpocznijWyjazd.visibility = View.INVISIBLE
                }
                else {
                    viewModel.statusKierowcy.postValue(driversStatus)
                    btn_RozpocznijWyjazd.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Timber.d("Vehicle not selected")
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WyborPojazdu().apply {
                arguments = Bundle().apply {

                }
            }
    }
}