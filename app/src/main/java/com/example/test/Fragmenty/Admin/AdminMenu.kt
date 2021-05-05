package com.example.test.Fragmenty.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.Adapter.DriverDataClass
import com.example.test.R
import kotlinx.android.synthetic.main.fragment_admin_menu.*
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


open class AdminMenu : Fragment() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromFirebaseAdmin()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        /*val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_adminMenu_to_adminDeparture)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)*/

        return inflater.inflate(R.layout.fragment_admin_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)


        btn_Kierowcy.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminMenu_to_adminDriverList)
        }
        btn_Pojazdy.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminMenu_to_adminVehicleList)
        }
        btn_Rozkazy.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminMenu_to_adminDeparture)
        }
    }



    fun getDataFromFirebaseAdmin(){
        var database = FirebaseDatabase.getInstance()
        val vehicleRef = database.getReference("Pojazdy")
        val driverRef = database.getReference("Kierowcy")
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)



        driverRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val testList = it.key.toString()
                    if (it.key.toString().isNotEmpty()){
                        viewModel.driverListIDs.add(testList)
                    }

                }

                viewModel.driverListIDs.removeLast()
                viewModel.driverListIDs.removeLast()


                for ((index, value) in viewModel.driverListIDs.withIndex()) {
                    driverRef.child(viewModel.driverListIDs[index]).child("Imię i nazwisko").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            viewModel.driverNameList.add(snapshot.getValue()
                                .toString())



                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                    driverRef.child(viewModel.driverListIDs[index]).child("Status kierowcy").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            viewModel.driverStatusList.add(snapshot.getValue()
                                .toString())
                            val dataForRecycler = DriverDataClass(
                                driverID = viewModel.driverListIDs[index],
                                driverFullName = viewModel.driverNameList[index],
                                driverStatus = viewModel.driverStatusList[index]
                            )
                            viewModel.adminDriversList.add(dataForRecycler)


                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })




                }


            }

            override fun onCancelled(error: DatabaseError) {

            }


        })



        vehicleRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val vehicleList = snapshot.getValue()
                    .toString()
                    .removePrefix("[")
                    .removeSuffix("]")
                    .split(",")
                    .toMutableList()

                vehicleList.removeAt(0)
                for ((index,value) in vehicleList.withIndex()){
                    viewModel.adminVehicleList.add(value)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })





    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AdminMenu().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}