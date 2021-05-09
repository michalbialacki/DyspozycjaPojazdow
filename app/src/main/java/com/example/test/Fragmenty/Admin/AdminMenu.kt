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
import com.google.firebase.database.*


class AdminMenu : Fragment(), AdapterPositionInterface, BackPressed {
    private lateinit var viewModel: ViewModelSystemuDyspozycji
    private var database = FirebaseDatabase.getInstance()
    private var driverRef = database.getReference("Kierowcy")


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
        viewModel.vehicleList.clear()


        rcl_DriverRecycler.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL, false
        )
        rcl_DriverRecycler.adapter = DriverAdapter(viewModel.adminDriversList, this)
//        viewModel.driverAdapterPosition.observe(viewLifecycleOwner, Observer {
//
//            if (it != -1) {
//                Navigation.findNavController(requireView()).navigate(R.id.driverSelected)
//                viewModel.driverAdapterPosition.removeObservers(viewLifecycleOwner)
//            }
//        })



        rcl_VehicleAdapter.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL, false
        )
        rcl_VehicleAdapter.adapter = VehicleAdapter(viewModel.adminVehicleList, this)
        viewModel.vehicleAdapterPosition.observe(viewLifecycleOwner, Observer {

            if (it != -1) {

                Navigation.findNavController(requireView()).navigate(R.id.vehicleSelected)
                viewModel.vehicleAdapterPosition.removeObservers(viewLifecycleOwner)
            }
        })


             btn_addDriver.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminMenu_to_addDriver)
        }
        /*btn_Pojazdy.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminMenu_to_adminVehicleList)
        }
        btn_Rozkazy.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminMenu_to_adminDeparture)
        }*/

    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminMenu().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onDriverClicked(position: Int) {
        viewModel.driverAdapterPosition.postValue(position)
        getSelectedDriverVehicles(
            driverRef = driverRef.child(viewModel.driverListIDs[position])
                .child("Przypisane pojazdy"))
    }

    override fun onVehicleClicked(position: Int) {
        viewModel.vehicleAdapterPosition.postValue(position)
    }

    override fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel =
                    ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
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

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        Navigation.findNavController(requireView()).navigate(R.id.logowanie)
    }

    fun getSelectedDriverVehicles(driverRef: DatabaseReference) {
        driverRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val testList = it.value.toString()
                    if (it.toString().isNotEmpty()) {
                        viewModel.vehicleList.add(testList)
                    }

                }
                Navigation.findNavController(requireView()).navigate(R.id.driverSelected)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}