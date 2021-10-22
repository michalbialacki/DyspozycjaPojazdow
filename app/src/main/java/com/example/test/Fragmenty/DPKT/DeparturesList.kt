package com.example.test.Fragmenty.DPKT

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.Adapter.DepartureAdapter
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_departures_list.*

private var database = FirebaseDatabase.getInstance()
private var driverRef = database.getReference("Kierowcy")
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
            if (viewModel.IDUzytkownika.value != "4412112244"){
                getDataStamp()
            }
        }




    }



    companion object {

    }


    private fun getDataStamp() {
        driverRef.child(viewModel.IDUzytkownika.value.toString()).child("DataStamp").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModel.changeDataStamp(snapshot.value.toString())
                Toast.makeText(requireContext(),"Identyfikator wyjazdu: ${snapshot.value.toString()} ",Toast.LENGTH_SHORT).show()


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    override fun onDriverClicked(position: Int) {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.departure_popup,null)
        val driverID : TextView = dialogLayout.findViewById<TextView>(R.id.edt_driverIDforPST)
            with(builder){
                setTitle("Zatwierdź wyjazd")
                if (viewModel.IDUzytkownika.value=="4412112244"){
                    setPositiveButton("Potwierdzam"){ dialogInterface: DialogInterface, i: Int ->
                        if (driverID.text.isEmpty()){
                            Toast.makeText(requireContext(),"Brak ID kierowcy",Toast.LENGTH_SHORT).show()
                        }
                        else{
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

                            Log.d(TAG, "TESTSET: ${driverID.text}")
                            driverRef.child(driverID.text.toString()).child("DataStamp").setValue("${viewModel.DayForUser}")

                            //Navigation.findNavController(requireView()).navigate(R.id.rozliczenie2)
                        }
                        }

                    setNegativeButton("Nie wyjeżdżam"){ dialogInterface: DialogInterface, i: Int ->
                    }
                }
                else{
                    //driverID.visibility = View.INVISIBLE
                    driverID.hint = "Podaj identyfikator wyjazdu"
                    setPositiveButton("Zrealizuj"){ dialogInterface: DialogInterface, i: Int ->
                        if (driverID.text.isNotEmpty())
                        {
                            viewModel.changeDataStamp(driverID.text.toString())
                            Navigation.findNavController(requireView()).navigate(R.id.rozliczenie2)
                        }
                        else{
                            Toast.makeText(requireContext(),"Brak identyfiktaora wyjazdu",Toast.LENGTH_SHORT).show()
                        }

                    }
                    setNegativeButton("Usuń"){ dialogInterface: DialogInterface, i: Int ->
                        viewModel.departureList.removeAt(position)
                        rcl_DepartureRecycler.adapter!!.notifyDataSetChanged()
                    }
                }


                setView(dialogLayout)
                show()
            }




        //viewModel.departureList.removeAt(position)


    }

    override fun onVehicleClicked(position: Int) {
    }
}