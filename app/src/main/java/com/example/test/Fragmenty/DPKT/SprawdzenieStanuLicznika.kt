package com.example.test.Fragmenty.DPKT

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sprawdzenie_stanu_licznika.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SprawdzenieStanuLicznika : Fragment() {

    private var database = FirebaseDatabase.getInstance()
    private lateinit var viewModel : ViewModelSystemuDyspozycji

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sprawdzenie_stanu_licznika, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        var referencjaKierowcy = database.getReference("Kierowcy").child(viewModel.IDUzytkownika.value.toString()).child("Status kierowcy")
        var referencjaPojazdu = database.getReference("Pojazdy").child(viewModel.RejestracjaPojazdu.value.toString())

        lt_swipeable.setOnRefreshListener {
            val potwierdzonyStanLicznika = edt_StanLicznikaPojazdu.text.toString().toDouble().toString()
            lt_swipeable.isRefreshing = false
            if(viewModel.statusPojazdu.value.equals("Dostępny")){
                wyjazdPojazdu(referencjaPojazdu,referencjaKierowcy,potwierdzonyStanLicznika)
                Toast.makeText(requireContext(), "Kierowca może wyjechać",Toast.LENGTH_SHORT).show()

            }
            else {
                powrotPojazdu(referencjaPojazdu,referencjaKierowcy,potwierdzonyStanLicznika)
            }
            Navigation.findNavController(requireView()).navigate(R.id.action_sprawdzenieStanuLicznika_to_wprowadzenieSprawdzenieDanychKierowcy)
        }
        }


    private fun powrotPojazdu(referencjaPojazdu : DatabaseReference, referencjaKierowcy : DatabaseReference, potwierdzonyStanLicznika : String) {
        referencjaPojazdu.child("Stan licznika").setValue(potwierdzonyStanLicznika)
        referencjaPojazdu.child("Status").setValue("Dostępny")
        referencjaKierowcy.setValue("Powrócił")

    }


    private fun wyjazdPojazdu (referencjaPojazdu : DatabaseReference, referencjaKierowcy : DatabaseReference, potwierdzonyStanLicznika : String) {
        referencjaPojazdu.child("Stan licznika").setValue(potwierdzonyStanLicznika)
        referencjaPojazdu.child("Status").setValue("Niedostępny")
        referencjaKierowcy.setValue("W trasie")
    }

    companion object {


        fun newInstance(param1: String, param2: String) =
            SprawdzenieStanuLicznika().apply {
            }
    }
}