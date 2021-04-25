package com.example.test

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.Adapter.DaneRecycler
import com.example.test.DataClasses.PozycjaTablicy
import com.example.test.LiveDataProjektu.LocationViewModel
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cardview.*
import kotlinx.android.synthetic.main.fragment_nawigacyjny.*
import java.util.*

class start : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationrequest: LocationRequest
    private lateinit var viewModel: ViewModelSystemuDyspozycji
    private lateinit var locViewModel : LocationViewModel
    private lateinit var database: DatabaseReference
    lateinit var myRef: DatabaseReference
    lateinit var TablicaRozliczenia : MutableList<PozycjaTablicy>

    var clickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        locViewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
        var trasaDatabase = "Punkt trasy"
        val database = FirebaseDatabase.getInstance()
        val vehicleReference = database.getReference("Pojazdy")
        num_OdometerInput.hint = "${viewModel.PoczatkowyStanLicznika.value.toString()}"
        TablicaRozliczenia = mutableListOf()

        localizationDeclaration()
        updateCurrentLoc()
        //getUserLocation()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)


        locViewModel.getLocationData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val localizationMessage = it.toString()
                .removePrefix("LocationModel(")
                .removeSuffix(")")
                .split(",")
                .toTypedArray()
            tv_Longtitude.text = "Longitude= " + localizationMessage.first()
                .removePrefix("longitude=")
            tv_Latitude.text = "Latitude= " + localizationMessage.last()
                .removePrefix(" latitude=")
        })

        lt_Swipe.setOnRefreshListener {
            lt_Swipe.isRefreshing=false
            val odometerCount = num_OdometerInput.text?.toString()

            getUserLocation()
            if (odometerCount.isNullOrEmpty()){
                Toast.makeText(activity,
                    "Wprowadź stan licznika!",
                    Toast.LENGTH_SHORT).show()
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                return@setOnRefreshListener
            }
            if (odometerCount.toDouble()!! < viewModel.PoczatkowyStanLicznika.value.toDouble()){
                Toast.makeText(requireContext(), "Niepoprawny stan licnzika!!")
            }
            val kmInput = odometerCount.toDouble() -  viewModel.PoczatkowyStanLicznika.value!!
            val routeCheckpoint = locViewModel.getLocationData().value.toString().removePrefix("LocationModel(").removeSuffix(")").split(",").toTypedArray()
            val routeCheckpointMessage = routeCheckpoint.last().removePrefix(" latitude=") +","+ routeCheckpoint.first().removePrefix("longitude=")
            val rowToAdapterView = DaneRecycler(routeCheckpointMessage,odometerCount,kmInput.toString())
            viewModel.PoczatkowyStanLicznika.postValue(odometerCount.toDouble())
            viewModel.dodajDoRozliczenia(rowToAdapterView)
            vehicleReference.child(viewModel.RejestracjaPojazdu.value.toString())
                .child("LokPojazdu")
                .setValue(routeCheckpointMessage)
            Toast.makeText(requireContext(),
                "Sprawdź poprawność wprowadzonego punktu!",
                Toast.LENGTH_SHORT).show()

        }

btn_Nawigacja.setOnClickListener {
    Navigation.findNavController(view).navigate(R.id.action_Nawigacyjny_to_rozliczenie2)
}
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nawigacyjny,container,false)
    }

//%%%%%%%%%%%%%Miejsce na funkcje %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


    private fun localizationDeclaration(){
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context!!)
        locationrequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                }
            }
        }
    }


    private fun checkPermits() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
    }



    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        checkPermits()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            updateLiveData(location?.longitude,location?.latitude)
        }

    }
    private fun updateLiveData (latitude : Double?, longtitude : Double?) {

        val punktTrasy= latitude.toString() + ";" + longtitude.toString()
                tv_Latitude.text = "Zapisano punkt"
                tv_Longtitude.text = "Zapisano punkt"
                viewModel.zapiszPunktTrasy(punktTrasy)

        }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLoc() {
        checkPermits()

        fusedLocationProviderClient.requestLocationUpdates(
            locationrequest,
            locationCallback,
            Looper.getMainLooper()
        )


    }



}