package com.example.test.Fragmenty.DPKT

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.FirebaseDatabase

private var database = FirebaseDatabase.getInstance()
private var myRef = database.getReference("Kierowcy")
private lateinit var viewModel : ViewModelSystemuDyspozycji
private var zmiennaDoTestowaniaWybieraniaZmiennej : String = ""

class DeparturesList : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_departures_list, container, false)
    }

    companion object {

    }
}