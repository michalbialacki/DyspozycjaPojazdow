package com.example.test.Fragmenty.DPKT

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_wprowadzenie_sprawdzenie_danych_kierowcy.*


class DriverCheckout : Fragment() {
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("Kierowcy")
    private lateinit var viewModel : ViewModelSystemuDyspozycji
    private var zmiennaDoTestowaniaWybieraniaZmiennej : String = ""

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
        return inflater.inflate(
            R.layout.fragment_wprowadzenie_sprawdzenie_danych_kierowcy,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        val database = FirebaseDatabase.getInstance()
        var referencjaKierowcy = database.getReference("Kierowcy")
        var referencjaPojazdu = database.getReference("Pojazdy")
        btn_Dalej.visibility = View.INVISIBLE





        lt_swipable.setOnRefreshListener {
            lt_swipable.isRefreshing = false
            val numerRejestracyjny = edt_WprowadzNrRejestracyjny.text.toString()
            val idKierowcy = edt_IdKierowcy.text.toString()
            referencjaKierowcy = referencjaKierowcy.child(idKierowcy).child("Przypisane pojazdy")
            referencjaPojazdu = referencjaPojazdu.child(numerRejestracyjny).child("Status")
            podajWartoscZFirebase(referencjaKierowcy,numerRejestracyjny, referencjaPojazdu)

            //Navigation.findNavController(requireView()).navigate(R.id.action_wprowadzenieSprawdzenieDanychKierowcy_to_sprawdzenieStanuLicznika)


        }


            btn_Dalej.setOnClickListener {
                Navigation.findNavController(requireView()).navigate(R.id.action_wprowadzenieSprawdzenieDanychKierowcy_to_sprawdzenieStanuLicznika)


        }


    }






    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%FUNKCJE%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%




    private fun podajWartoscZFirebase (postRef: DatabaseReference,numerRejestracyjny:String, pojazdRef : DatabaseReference) {

        pojazdRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val statusPojazdu = snapshot.getValue().toString()
                viewModel.statusPojazdu.postValue(statusPojazdu)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        postRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)  {
                if (dataSnapshot.getValue().toString().isEmpty()) {
                    "Błędne dane! Popraw wprowadzone dane!"
                }
                else{
                    zmiennaDoTestowaniaWybieraniaZmiennej = dataSnapshot.getValue().toString()
                    zbierzListePojazdow(zmiennaDoTestowaniaWybieraniaZmiennej,numerRejestracyjny)



                }
            }
            override fun onCancelled(databaeError: DatabaseError) {

            }



        })





    }

   private fun zbierzListePojazdow(wartosc : String, numerRejestracyjny : String){

        var listaPojazdow = wartosc.removePrefix("[").removeSuffix("]").split(", ").toMutableList()
        listaPojazdow.removeAt(0)

        for((index,value) in listaPojazdow.withIndex()){
            value.removePrefix(" ")

            if (value.equals(numerRejestracyjny)){
                viewModel.RejestracjaPojazdu.postValue(value)
                btn_Dalej.visibility = View.VISIBLE
            }
            else continue
            }
        }








    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DriverCheckout().apply {
                arguments = Bundle().apply {

                }
            }
    }
}



