package com.example.test.Fragmenty.Admin

import android.content.ContentValues.TAG
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.TokenWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_vehicle_selected.*

class CheckpointTable : Fragment(), BackPressed {
    private lateinit var viewModel: ViewModelSystemuDyspozycji
    var selectedVehicleLat = mutableListOf<Double>()
    var selectedVehicleLng = mutableListOf<Double>()


    private val callback = OnMapReadyCallback { googleMap ->

        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        var vehicleAdapterPosition = viewModel.vehicleAdapterPosition.value
            var coordinatesList = mutableListOf<LatLng>()
            selectedVehicleLat.forEachIndexed() { index, s ->
                coordinatesList.add(LatLng(selectedVehicleLat[index],selectedVehicleLng[index]))
                googleMap.addMarker(MarkerOptions().position(coordinatesList[index]).title("Punkt ${index}"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinatesList[index],5f))
            }




        }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vehicle_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        viewModel.latTable.forEach {
            selectedVehicleLat.add(it)
        }
        viewModel.lngTable.forEach {
            selectedVehicleLng.add(it)
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.location_map) as SupportMapFragment?
        var vehicleAdapterPosition = viewModel.vehicleAdapterPosition.value
        Log.e(TAG, "${selectedVehicleLat}  ${selectedVehicleLng}", )
        mapFragment?.getMapAsync(callback)
        tv_VehicleType.text = viewModel.vehicleType[vehicleAdapterPosition!!]
        tv_SelectedVehicleStatus.text = viewModel.vehicleStatus[vehicleAdapterPosition!!]
        tv_VehicleOdometer.text = viewModel.vehicleOdometer[vehicleAdapterPosition!!].toString()

    }

    override fun backPressed() {
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

        viewModel.vehicleAdapterPosition.observe(viewLifecycleOwner, Observer {
            if (it==-1)
                Navigation.findNavController(requireView()).navigate(R.id.adminMenu)
        })

    }
}