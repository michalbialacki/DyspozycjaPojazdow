package com.example.test.Fragmenty.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.Adapter.DriverDataClass
import com.example.test.Adapter.adminVehicleDataClass
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_logowanie.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Logowanie : Fragment() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("Kierowcy")
    private lateinit var viewModel : ViewModelSystemuDyspozycji



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logowanie, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        var driversID : String = ""
        var driversPass:String = ""
        val newReferenceLogin = myRef.child("${viewModel.IDUzytkownika.value}").child("Przypisane pojazdy")
        saveVehicleList(newReferenceLogin)
        getDataFromFirebaseAdmin()



        btn_ConfirmDriverLoginData.setOnClickListener {

            driversID = edt_DriverLogin.text!!.toString()
            driversPass = edt_GetDriverPass.text!!.toString()
            val postRef = myRef.child(driversID).child("Haslo")
            userLogin(postRef,driversPass,driversID, newReferenceLogin)


        }
    }

    private fun userLogin (postRef: DatabaseReference, driversPass : String, idKierowcy : String, nowyRef: DatabaseReference){
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val snapshotValue = dataSnapshot.getValue().toString()
                userCheck(snapshotValue, driversPass, idKierowcy,nowyRef)

            }
            override fun onCancelled(databaeError: DatabaseError) {
            }

            })


        }


    private fun saveVehicleList (newReferenceLogin: DatabaseReference){
        newReferenceLogin.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val datasnapshotValue = dataSnapshot.getValue().toString().removePrefix("[").removeSuffix("]")
                viewModel.dodajPojazdDoListyWyboru(datasnapshotValue)
            }

                override fun onCancelled(databaeError: DatabaseError) {

                }

        })
    }


    private fun userCheck(driversPasswordFirebase : String, driversPass: String, driversID: String, newReferenceLogin: DatabaseReference) {
        if (driversPasswordFirebase.isNotBlank()) {
            if (driversPass.equals(driversPasswordFirebase)) {


                when (driversID) {
                    "4412112244" -> {
                        viewModel.IDUzytkownika.postValue("4412112244")
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_logowanie_to_wprowadzenieSprawdzenieDanychKierowcy)
                    }
                    "6568777378" ->{
                    viewModel.ZapiszDaneUzytkownika(driversPass, driversID)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_logowanie_to_adminMenu)
                    }
                    else -> {
                        viewModel.ZapiszDaneUzytkownika(driversPass, driversID)
                        saveVehicleList(newReferenceLogin)
                        getName(driversID)
                        getDataStamp(driversID)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_logowanie_to_wyborPojazdu)
                    }
                }

            } else {
                Toast.makeText(requireContext(), "B????dne has??o lub ID!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getName(driversID: String) {
        myRef.child(driversID).child("Imi?? i nazwisko").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.driversName.postValue(snapshot.value.toString())


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun getDataStamp(driversID: String) {
        myRef.child(driversID).child("DataStamp").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.changeDataStamp(snapshot.value.toString())


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    ////////////////////////GETDATA FOR ADMIN////////////////////////////////////////

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
                    val testList = it.key
                            .toString()
                    if (it.key
                                    .toString()
                                    .isNotEmpty()){
                        viewModel.driverListIDs.add(testList)
                    }

                }

                viewModel.driverListIDs.removeLast()
                viewModel.driverListIDs.removeLast()


                for ((index, value) in viewModel.driverListIDs.withIndex()) {
                    driverRef.child(viewModel.driverListIDs[index])
                            .child("Imi?? i nazwisko")
                            .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            viewModel.driverNameList.add(snapshot.getValue()
                                .toString())



                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                    driverRef.child(viewModel.driverListIDs[index])
                            .child("Status kierowcy")
                            .addListenerForSingleValueEvent(object :
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
        fun newInstance(param1: String, param2: String) =
            Logowanie().apply {

            }
    }
}