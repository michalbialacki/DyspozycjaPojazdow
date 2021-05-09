package com.example.test.Fragmenty.Admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_driver.*
import kotlinx.android.synthetic.main.fragment_wybor_pojazdu.*
import kotlin.random.Random

class AddDriver : Fragment(), BackPressed {
private lateinit var viewModel : ViewModelSystemuDyspozycji
    private var database = FirebaseDatabase.getInstance()
    private var driverRef = database.getReference("Kierowcy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_driver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)

        var newDriverID = (10000000..99999999).random().toString()
        val vehicleSelection = viewModel.vehicleListIDs
        val vehicleSpinner = view.findViewById<Spinner>(R.id.spn_AddVehicle)
        val vehicleAdapter = ArrayAdapter<String>(requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            vehicleSelection)
        vehicleSpinner.adapter = vehicleAdapter

        checkDriverID(newDriverID)

        btn_Accpet.setOnClickListener {

            var driverName = edt_DriverNames.text.toString()
            var driverPassword = edt_DriverPassword.text.toString()
            var passControl = edt_PassControl.text.toString()
            if (passControl.equals(driverPassword)){
                if (driverName.isNotEmpty()){
                    driverRef.child(newDriverID)
                        .child("Imię i nazwisko")
                        .setValue(driverName)
                    driverRef.child(newDriverID)
                        .child("Status kierowcy")
                        .setValue("Dostępny")
                    driverRef.child(newDriverID)
                        .child("Haslo")
                        .setValue(driverPassword)
                    driverRef.child(newDriverID)
                        .child("Przypisane pojazdy")
                        .child("1")
                        .setValue(spn_AddVehicle.selectedItem)

                    Toast.makeText(requireContext(),"Dodano kierowcę $driverName",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(),"Podaj imię i nazwisko kierowcy!",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(requireContext(),"Hasła nie są zgodne!",Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun checkDriverID(newDriverID: String) {
        viewModel.driverListIDs.forEach {
            if (it.equals(newDriverID)) {
                var newDriverID = (10000000..99999999).random().toString()
                checkDriverID(newDriverID)
            }
        }
    }

    private fun selectVehicle() {
        spn_AddVehicle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var driversVehicle = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(),
                    "Wybierz pojazd!",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
    }



    companion object {

            }

    override fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel =
                    ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
                viewModel.driverListIDs.clear()
                viewModel.vehicleListIDs.clear()
                viewModel.adminDriversList.clear()
                viewModel.adminVehicleList.clear()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        Navigation.findNavController(requireView()).navigate(R.id.adminMenu)
    }
    }
