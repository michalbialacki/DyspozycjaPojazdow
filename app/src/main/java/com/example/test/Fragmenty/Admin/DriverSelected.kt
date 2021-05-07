package com.example.test.Fragmenty.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.test.Interfaces.BackPressed
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import kotlinx.android.synthetic.main.fragment_driver_selected.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DriverSelected.newInstance] factory method to
 * create an instance of this fragment.
 */
class DriverSelected : Fragment(), BackPressed {
    private lateinit var viewModel: ViewModelSystemuDyspozycji
    private var param1: String? = null
    private var param2: String? = null

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

        return inflater.inflate(R.layout.fragment_driver_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        viewModel.adapterPositionViewModel.postValue(-1)
        tv_selectedDriverName.text = "Imię i nazwisko: " + viewModel.adminDriversList[viewModel.adapterPositionViewModel.value!!].driverFullName
        tv_SelectedDriverStatus.text = "Status" + viewModel.adminDriversList[viewModel.adapterPositionViewModel.value!!].driverStatus

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DriverSelected.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DriverSelected().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun backPressed() {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel= ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
                viewModel.driverListIDs.clear()
                viewModel.vehicleListIDs.clear()
                viewModel.adminDriversList.clear()
                viewModel.adminVehicleList.clear()
                viewModel.adapterPositionViewModel.postValue(-1)
                Navigation.findNavController(requireView()).navigate(R.id.adminMenu)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }
}