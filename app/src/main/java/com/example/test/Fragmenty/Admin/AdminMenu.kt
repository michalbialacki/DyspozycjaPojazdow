package com.example.test.Fragmenty.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.test.R
import kotlinx.android.synthetic.main.fragment_admin_menu.*


class AdminMenu : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_Kierowcy.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminMenu_to_adminDriverList)
        }
        btn_Pojazdy.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminMenu_to_adminVehicleList)
        }
        btn_Rozkazy.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminMenu_to_adminDeparture)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AdminMenu().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}