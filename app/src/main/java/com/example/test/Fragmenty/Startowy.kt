package com.example.test

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_startowy.*
import java.text.SimpleDateFormat
import java.util.*


class Startowy : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var myRef: DatabaseReference
    private lateinit var viewModel: ViewModelSystemuDyspozycji
    var tablicaProgresu = mutableListOf<String>("0", "0", "0", "0")
    var wskaznikPunktuStartu = 0
    var wskaznikPunktuKoncowego = 0


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
        return inflater.inflate(R.layout.fragment_startowy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Rozkazy wyjazdu").child("${viewModel.RejestracjaPojazdu.value}").child("${viewModel.Dzien}")
        val newRef = database.getReference("Kierowcy").child("${viewModel.IDUzytkownika.value}").child("Przypisane pojazdy")


        drugiDysponentDopisz()
        wybranieRodzajuPrzewozu()
        wybranieCeluWyjazdu()
        wprowadzenieSkadDokad()





        btn_DoNawigacji.setOnClickListener {
            sprawdzWarunki(tablicaProgresu)
            tworzenieRozkazuWyjazdu(myRef)
            Navigation.findNavController(view).navigate(R.id.action_startowy_to_rozliczenie2)
        }

    }

    //%%%%%%%%%%%FUNCTION SPACE%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    private fun wybranieCeluWyjazdu() {
        spn_CelWyjazdu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var celWyjazdu = parent?.getItemAtPosition(position).toString()
                viewModel.zapiszCelWyjazdu(celWyjazdu)
                Toast.makeText(
                    requireContext(),
                    "Zapisano cel wyjazdu: $celWyjazdu",
                    Toast.LENGTH_SHORT
                ).show()
                tablicaProgresu[2] = "1"

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Wybierz cel wyjazdu!", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun wybranieRodzajuPrzewozu() {
        spn_RodzajPrzewozu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var rodzajPrzewozu = parent?.getItemAtPosition(position).toString()
                viewModel.zapiszRodzajPrzeowzu(rodzajPrzewozu)
                tablicaProgresu[3] = "1"
                Toast.makeText(
                    requireContext(),
                    "Zapisano rodzaj przewozu: $rodzajPrzewozu",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Wybierz rodzaj przewozu!", Toast.LENGTH_LONG)
                    .show()
                return
            }

        }
    }


    private fun drugiDysponentDopisz() {
        edt_DrugiDysponent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_DrugiDysponent.text.length > 0) {
                    tablicaProgresu.set(0, "1")


                } else {
                    Toast.makeText(
                        requireContext(),
                        "Wprowadź drugiego dysponenta!",
                        Toast.LENGTH_SHORT
                    ).show()
                    tablicaProgresu.set(0, "0")

                }

            }

            override fun afterTextChanged(s: Editable?) {

                if (edt_DrugiDysponent.text.length > 0) {
                    viewModel.DrugiDysponentDodaj(edt_DrugiDysponent.text.toString())
                    Toast.makeText(
                        requireContext(),
                        "Wprowadzono drugiego dysponenta!", Toast.LENGTH_SHORT).show()
                    sprawdzWarunki(tablicaProgresu)

                } else {
                }
            }

        })


    }

    private fun wprowadzenieSkadDokad() {
        edt_PunktKoncowy.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_PunktKoncowy.text.length > 0) {
                    viewModel.PunktKoncowyZapisz(edt_PunktKoncowy.text.toString())
                    wskaznikPunktuKoncowego = 1
                    sprawdzWskaznikObecnosciPunktowTrasy(
                        wskaznikPunktuStartu,
                        wskaznikPunktuKoncowego
                    )

                } else {
                    Toast.makeText(requireContext(), "Wprowadź punkt końcowy!", Toast.LENGTH_SHORT)
                        .show()
                    wskaznikPunktuKoncowego = 0
                    sprawdzWskaznikObecnosciPunktowTrasy(
                        wskaznikPunktuStartu,
                        wskaznikPunktuKoncowego
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (edt_PunktKoncowy.text.length > 0) {
                    viewModel.PunktKoncowyZapisz(edt_PunktKoncowy.text.toString())
                    Toast.makeText(
                        requireContext(),
                        "Wprowadzono punkt końcowy trasy!",
                        Toast.LENGTH_SHORT
                    ).show()
                    sprawdzWskaznikObecnosciPunktowTrasy(
                        wskaznikPunktuStartu,
                        wskaznikPunktuKoncowego
                    )
                    sprawdzWarunki(tablicaProgresu)

                } else {
                }

            }
        })
        edt_PunktStartowy.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_PunktStartowy.text.length > 0) {
                    viewModel.PunktKoncowyZapisz(edt_PunktStartowy.text.toString())
                    wskaznikPunktuStartu = 1
                    sprawdzWskaznikObecnosciPunktowTrasy(
                        wskaznikPunktuStartu,
                        wskaznikPunktuKoncowego
                    )

                } else {
                    Toast.makeText(requireContext(), "Wprowadź punkt startowy!", Toast.LENGTH_SHORT)
                        .show()
                    wskaznikPunktuStartu = 0
                    sprawdzWskaznikObecnosciPunktowTrasy(
                        wskaznikPunktuStartu,
                        wskaznikPunktuKoncowego
                    )

                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (edt_PunktKoncowy.text.length > 0) {
                    viewModel.PunktStartowyZapisz(edt_PunktStartowy.text.toString())
                    Toast.makeText(
                        requireContext(),
                        "Wprowadzono punkt startowy trasy!",
                        Toast.LENGTH_SHORT
                    ).show()
                    sprawdzWskaznikObecnosciPunktowTrasy(
                        wskaznikPunktuStartu,
                        wskaznikPunktuKoncowego
                    )
                    sprawdzWarunki(tablicaProgresu)
                } else {
                }

            }
        })


    }

    private fun sprawdzWskaznikObecnosciPunktowTrasy(wskaznikPunktuStartu: Int,wskaznikPunktuKoncowego: Int) {
        if (wskaznikPunktuKoncowego === 1 && wskaznikPunktuStartu === 1) {
            tablicaProgresu.set(1, "1")
            viewModel.zapiszPunktTrasy("${edt_PunktStartowy.text} - ${edt_PunktKoncowy.text}")
        } else {
            tablicaProgresu.set(1, "0")
        }
    }

    private fun sprawdzWarunki(TablicaProgresu: MutableList<String>) {
        var key : Int = 1

        for (i in 0 until 3) {
            key*=TablicaProgresu[i].toInt()
        }

    if(key>0){
        btn_DoNawigacji.visibility=View.VISIBLE }
        else{
            btn_DoNawigacji.visibility = View.INVISIBLE

    }
}

    private fun tworzenieRozkazuWyjazdu (myRef: DatabaseReference){
        myRef.child("Drugi dysponent").setValue(viewModel.DrugiDysponent.value)
        myRef.child("Trasa").setValue(viewModel.PunktTrasy.value)
        myRef.child("Cel Wyjazdu").setValue(viewModel.CelWyjazdu.value)
        myRef.child("Rodzaj przewozu").setValue(viewModel.RodzajPrzewozu.value)
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Startowy().apply {
                arguments = Bundle().apply {

                }
            }
    }
}