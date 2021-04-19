package com.example.test.Fragmenty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_logowanie.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Logowanie.newInstance] factory method to
 * create an instance of this fragment.
 */
class Logowanie : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    //
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("Kierowcy")
    private lateinit var viewModel : ViewModelSystemuDyspozycji



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logowanie, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        var idKierowcy : String = ""
        var hasloKierowcy:String = ""
        val nowyRef = myRef.child("${viewModel.IDUzytkownika.value}").child("Przypisane pojazdy")
        zapisanieListyPojazdów(nowyRef)

        btn_ZatwierdzDaneKierowcy.setOnClickListener {
            idKierowcy = edt_IDKierowcy.text!!.toString()
            hasloKierowcy = edt_HasloKierowcy.text!!.toString()
            val postRef = myRef.child(idKierowcy).child("Haslo")
            zalogujUzytkownika(postRef,hasloKierowcy,idKierowcy, nowyRef)


        }
    }


    private fun zalogujUzytkownika (postRef: DatabaseReference, hasloKierowcy : String, idKierowcy : String, nowyRef: DatabaseReference){

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wartosc = dataSnapshot.getValue().toString()
                sprawdzaniedanychUzytkownika(wartosc, hasloKierowcy, idKierowcy,nowyRef)

            }
            override fun onCancelled(databaeError: DatabaseError) {

            }

            })


        }


    private fun zapisanieListyPojazdów (nowyRef: DatabaseReference){
        nowyRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wartosc = dataSnapshot.getValue().toString().removePrefix("[").removeSuffix("]")
                viewModel.dodajPojazdDoListyWyboru(wartosc)
            }


                override fun onCancelled(databaeError: DatabaseError) {

                }

        })
    }


    private fun sprawdzaniedanychUzytkownika(wartosc : String, hasloKierowcy: String, idKierowcy: String, nowyRef: DatabaseReference){
        if(wartosc.isNotBlank()){
            if (hasloKierowcy.equals(wartosc)){
                if (idKierowcy.equals("44121122") ){
                    Navigation.findNavController(requireView()).navigate(R.id.action_logowanie_to_wprowadzenieSprawdzenieDanychKierowcy)
                }
                else {
                    viewModel.ZapiszDaneUzytkownika(hasloKierowcy,idKierowcy)
                    zapisanieListyPojazdów(nowyRef)
                    Navigation.findNavController(requireView()).navigate(R.id.action_logowanie_to_wyborPojazdu)
                }

            }
                else {
                    Toast.makeText(requireContext(),"Błędne hasło!",Toast.LENGTH_SHORT).show()}
            }
            else {
                Toast.makeText(requireContext(),"Błędne ID",Toast.LENGTH_SHORT).show()

            }
        }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Logowanie.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Logowanie().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}