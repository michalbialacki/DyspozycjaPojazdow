package com.example.test.Interfaces

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface AdapterPositionInterface {
    fun onDriverClicked(position: Int)
    fun onVehicleClicked(position: Int)
}