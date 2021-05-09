package com.example.test.Fragmenty.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.Adapter.SelectedDriverAdapter
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import kotlinx.android.synthetic.main.fragment_driver_selected.*


class DriverSelected : Fragment(), BackPressed {
    private lateinit var viewModel: ViewModelSystemuDyspozycji

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
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        rcl_selectedDriverVehiclesAdapter.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL, false
        )
        rcl_selectedDriverVehiclesAdapter.adapter = SelectedDriverAdapter(viewModel.vehicleList)

        viewModel.driverAdapterPosition.postValue(-1)
        tv_selectedDriverName.text = "Imię i nazwisko: " + viewModel.adminDriversList[viewModel.driverAdapterPosition.value!!].driverFullName
        tv_SelectedDriverStatus.text = "Status: " + viewModel.adminDriversList[viewModel.driverAdapterPosition.value!!].driverStatus

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
}