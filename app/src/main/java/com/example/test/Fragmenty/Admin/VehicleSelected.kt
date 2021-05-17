package com.example.test.Fragmenty.Admin

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.Adapter.adminVehicleDataClass
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_driver_selected.*
import kotlinx.android.synthetic.main.fragment_vehicle_selected.*

class VehicleSelected : Fragment(), BackPressed {
    private lateinit var viewModel: ViewModelSystemuDyspozycji

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        val vehicleAdapterPosition = viewModel.vehicleAdapterPosition.value!!
        val selectedVehicleLatLng = viewModel.vehicleLastLocation[vehicleAdapterPosition]
                                    .split(" , ")
                                    .toMutableList()


        val sydney = LatLng(selectedVehicleLatLng[0].toDouble()!!,selectedVehicleLatLng[1].toDouble()!!)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Selected vehicle"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))
        tv_VehicleType.text = viewModel.vehicleType[vehicleAdapterPosition]
        tv_SelectedVehicleStatus.text = viewModel.vehicleStatus[vehicleAdapterPosition]
        tv_VehicleOdometer.text = viewModel.vehicleOdometer[vehicleAdapterPosition].toString()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vehicle_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.location_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

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