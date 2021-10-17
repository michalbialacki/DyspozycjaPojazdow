package com.example.test.Fragmenty.DPKT

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.Adapter.DepartureAdapter
import com.example.test.Adapter.DeparturesDataClass
import com.example.test.Adapter.DriverAdapter
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_admin_vehicle_list.*
import kotlinx.android.synthetic.main.fragment_departures_list.*
import kotlinx.android.synthetic.main.fragment_driver_checkout.*

private var database = FirebaseDatabase.getInstance()
private var myRef = database.getReference("Kierowcy")
private var ordersRef = database.getReference("Rozkazy wyjazdu")
private lateinit var viewModel : ViewModelSystemuDyspozycji


class DeparturesList : Fragment(), AdapterPositionInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_departures_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        rcl_DepartureRecycler.layoutManager = LinearLayoutManager(activity)
        rcl_DepartureRecycler.adapter = DepartureAdapter(viewModel.departureList, this)
        lt_RefreshList.setOnRefreshListener {
            lt_RefreshList.isRefreshing = false
        }
        if (viewModel.IDUzytkownika.value.equals("4412112244")){
            Toast.makeText(requireContext(),"XD",Toast.LENGTH_SHORT).show()
        }


    }



    companion object {

    }


    override fun onDriverClicked(position: Int) {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.departure_popup,null)
        if (viewModel.IDUzytkownika.value == "4412112244") {

        }
        else{
            with(builder){
                setTitle("Zatwierdź wyjazd")
                if (viewModel.IDUzytkownika.value!="4412112244"){
                    setPositiveButton("Potwierdzam"){ dialogInterface: DialogInterface, i: Int ->
                        ordersRef.child(viewModel.departureList[position].vehicleID)
                            .child(viewModel.DayForUser)
                            .child("Cel wyjazdu")
                            .setValue(viewModel.departureList[position].departurePurpose)
                        ordersRef.child(viewModel.departureList[position].vehicleID)
                            .child(viewModel.DayForUser)
                            .child("Rodzaj przewozu")
                            .setValue(viewModel.departureList[position].departureType)
                        ordersRef.child(viewModel.departureList[position].vehicleID)
                            .child(viewModel.DayForUser)
                            .child("Trasa")
                            .setValue(viewModel.departureList[position].departureRoute)
                        ordersRef.child(viewModel.departureList[position].vehicleID)
                            .child(viewModel.DayForUser).child("Drugi dysponent")
                            .setValue(viewModel.departureList[position].departureDisposer)


                        Navigation.findNavController(requireView()).navigate(R.id.rozliczenie2)
                    }
                    setNegativeButton("Nie wyjeżdżam"){ dialogInterface: DialogInterface, i: Int ->
                        Toast.makeText(requireContext(),"PRZEGRAŁ",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    setPositiveButton("Wyjazd"){ dialogInterface: DialogInterface, i: Int ->
                        viewModel.departureList.removeAt(position)
                        rcl_DepartureRecycler.adapter!!.notifyDataSetChanged()
                    }
                    setNegativeButton("Nie wyjazd"){ dialogInterface: DialogInterface, i: Int ->
                        Toast.makeText(requireContext(),"PRZEGRAŁ",Toast.LENGTH_SHORT).show()
                    }
                }


                setView(dialogLayout)
                show()
            }
        }



        //viewModel.departureList.removeAt(position)


    }

    override fun onVehicleClicked(position: Int) {
    }
}