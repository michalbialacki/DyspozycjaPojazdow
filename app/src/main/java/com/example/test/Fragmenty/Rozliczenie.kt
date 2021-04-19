package com.example.test.Fragmenty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.Adapter.DaneRecycler
import com.example.test.Adapter.SysDyspAdapter
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_rozliczenie.*

class Rozliczenie : Fragment() {

    private lateinit var viewModel : ViewModelSystemuDyspozycji

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.logowanie)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rozliczenie, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Rozkazy wyjazdu").child("${viewModel.RejestracjaPojazdu.value}").child("${viewModel.Dzien}")
        val postRef = myRef.child("Rozliczenie Pojazdu")
        var i : Int = 0

        rcl_RecyclerView.adapter = SysDyspAdapter(viewModel.RozliczeniePojazdu)
        rcl_RecyclerView.layoutManager = LinearLayoutManager(activity)
            if (viewModel.RozliczeniePojazdu.size >0){
                tv_StartText.visibility = View.INVISIBLE
            }


        czytajDaneZFireBase(postRef)
        Toast.makeText(requireContext(),"${viewModel.PoczatkowyStanLicznika.value}",Toast.LENGTH_SHORT).show()

        btn_DodajPunktTrasy.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_rozliczenie2_to_Nawigacyjny)
        }

        btn_ZakonczPodroz.setOnClickListener {
            myRef.child("Rozliczenie Pojazdu").setValue(viewModel.RozliczeniePojazdu)
            Navigation.findNavController(view).navigate(R.id.action_rozliczenie2_to_wyborPojazdu)
        }

    }


    private fun czytajDaneZFireBase (postRef: DatabaseReference){

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wartosc = dataSnapshot.getValue().toString().removePrefix("[{").removeSuffix("}]").split("},").toTypedArray()
            }
            override fun onCancelled(databaeError: DatabaseError) {

            }
        })

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Rozliczenie.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Rozliczenie().apply {

                }
            }
    }