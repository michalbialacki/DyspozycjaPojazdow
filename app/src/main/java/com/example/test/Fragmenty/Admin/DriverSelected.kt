package com.example.test.Fragmenty.Admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.Adapter.SelectedDriverAdapter
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_driver_selected.*


class DriverSelected : Fragment(), BackPressed, AdapterPositionInterface {
    private lateinit var viewModel: ViewModelSystemuDyspozycji
    private var database = FirebaseDatabase.getInstance()
    private var driverRef = database.getReference("Kierowcy").child("Przypisane pojazdy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_driver_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel =
            ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        rcl_selectedDriverVehiclesAdapter.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL, false
        )
        rcl_selectedDriverVehiclesAdapter.adapter = SelectedDriverAdapter(viewModel.vehicleList, this)

        tv_selectedDriverName.text =
            "Imię i nazwisko: " + viewModel.adminDriversList[viewModel.driverAdapterPosition.value!!].driverFullName
        tv_SelectedDriverStatus.text =
            "Status: " + viewModel.adminDriversList[viewModel.driverAdapterPosition.value!!].driverStatus

    }

    override fun onDestroy() {
        super.onDestroy()
        val viewModel =
            ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        viewModel.driverAdapterPosition.postValue(-1)
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DriverSelected().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun backPressed() {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel= ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
                viewModel.adminDriversList.clear()
                viewModel.adminVehicleList.clear()
                viewModel.driverListIDs.clear()
                viewModel.driverNameList.clear()
                viewModel.driverStatusList.clear()
                viewModel.vehicleListIDs.clear()
                viewModel.vehicleLastLocation.clear()
                viewModel.vehicleStatus.clear()
                viewModel.vehicleOdometer.clear()
                viewModel.vehicleType.clear()
                viewModel.vehicleList.clear()
                viewModel.driverAdapterPosition.postValue(-1)
                Navigation.findNavController(requireView()).navigate(R.id.adminMenu)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }

    override fun onDriverClicked(position: Int) {
    }

    override fun onVehicleClicked(position: Int) {
        val viewModel =
            ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        viewModel.vehicleAdapterPosition.postValue(position)
        viewModel.vehicleAdapterPosition.observe(viewLifecycleOwner, Observer {
            if (it !=-1){
                Navigation.findNavController(requireView()).navigate(R.id.addDeparture)
            }
        })

    }
}