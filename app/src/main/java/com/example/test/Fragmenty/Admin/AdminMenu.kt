package com.example.test.Fragmenty.Admin

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
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.Adapter.DriverAdapter
import com.example.test.Adapter.VehicleAdapter
import com.example.test.Interfaces.BackPressed
import com.example.test.R
import kotlinx.android.synthetic.main.fragment_admin_menu.*
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji


class AdminMenu : Fragment(), AdapterPositionInterface, BackPressed {
    private lateinit var viewModel: ViewModelSystemuDyspozycji





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_menu, container, false)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)


        rcl_DriverRecycler.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL, false
        )
        rcl_DriverRecycler.adapter = DriverAdapter(viewModel.adminDriversList, this)
        viewModel.adapterPositionViewModel.observe(viewLifecycleOwner, Observer {

            if (it != -1) {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                //viewModel.adapterPositionViewModel.postValue(it)
                Navigation.findNavController(requireView()).navigate(R.id.driverSelected)
                viewModel.adapterPositionViewModel.removeObservers(viewLifecycleOwner)
            }
        })



        rcl_VehicleAdapter.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL, false
        )
        rcl_VehicleAdapter.adapter = VehicleAdapter(viewModel.adminVehicleList, this)




        btn_addDriver.setOnClickListener {
//            Navigation.findNavController(requireView())
//                .navigate(R.id.action_adminMenu_to_adminDriverList)
        }
        btn_Pojazdy.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminMenu_to_adminVehicleList)
        }
        btn_Rozkazy.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminMenu_to_adminDeparture)
        }

    }




    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AdminMenu().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    override fun onItemClick(position: Int) {
        viewModel.adapterPositionViewModel.postValue(position)
    }

    override fun backPressed() {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel= ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
                viewModel.driverListIDs.clear()
                viewModel.vehicleListIDs.clear()
                viewModel.adminDriversList.clear()
                viewModel.adminVehicleList.clear()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        Navigation.findNavController(requireView()).navigate(R.id.logowanie)
    }


}