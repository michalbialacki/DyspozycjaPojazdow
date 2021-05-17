package com.example.test.Fragmenty.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.Adapter.DriverDataClass
import com.example.test.Adapter.adminVehicleDataClass
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_add_vehicle.*


class AddVehicle : Fragment(), BackPressed {

    private lateinit var viewModel: ViewModelSystemuDyspozycji
    private var database = FirebaseDatabase.getInstance()
    private var vehicleRef = database.getReference("Pojazdy")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_vehicle, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_AddVehicle.setOnClickListener{
            var vehicleID = edt_VehicleID.text.toString()
            var vehicleType = edt_Type.text.toString()
            var vehicleOdometer = edt_Odometer.text.toString()

            vehicleRef.child(vehicleID).child("LokPojazdu").setValue("53.2123 , 20.2111")
            vehicleRef.child(vehicleID).child("Marka i model").setValue(vehicleType)
            vehicleRef.child(vehicleID).child("Stan licznika").setValue(vehicleOdometer)
            vehicleRef.child(vehicleID).child("Status").setValue("Dostępny")


        }

    }



    fun getDataFromFirebaseAdmin(){
        var database = FirebaseDatabase.getInstance()
        val vehicleRef = database.getReference("Pojazdy")
        val driverRef = database.getReference("Kierowcy")
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)

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
                snapshot.children.forEach{
                    val testList = it.key.toString()
                    if (it.key.toString().isNotEmpty()){
                        viewModel.vehicleListIDs.add(testList)
                    }

                }

                for ((index, value) in viewModel.vehicleListIDs.withIndex()) {
                    vehicleRef.child(viewModel.vehicleListIDs[index]).child("LokPojazdu")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                viewModel.vehicleLastLocation.add(
                                    snapshot.getValue()
                                        .toString()
                                )
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                    vehicleRef.child(viewModel.vehicleListIDs[index]).child("Stan licznika")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                viewModel.vehicleOdometer.add(
                                    snapshot.getValue()
                                        .toString()
                                        .toDouble()
                                )
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                    vehicleRef.child(viewModel.vehicleListIDs[index]).child("Marka i model")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                viewModel.vehicleType.add(
                                    snapshot.getValue()
                                        .toString()
                                )
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                    vehicleRef.child(viewModel.vehicleListIDs[index]).child("Status")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                viewModel.vehicleStatus.add(
                                    snapshot.getValue()
                                        .toString()
                                )
                                var dataForRecycler = adminVehicleDataClass(
                                    viewModel.vehicleListIDs[index],
                                    viewModel.vehicleLastLocation[index],
                                    viewModel.vehicleLastLocation[index],
                                    viewModel.vehicleOdometer[index],
                                    viewModel.vehicleStatus[index]
                                )
                                viewModel.adminVehicleList.add(dataForRecycler)


                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) {

            }
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
        getDataFromFirebaseAdmin()
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        Navigation.findNavController(requireView()).navigate(R.id.adminMenu)
    }

}