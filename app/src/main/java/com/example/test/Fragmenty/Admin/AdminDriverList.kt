package com.example.test.Fragmenty.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.Interfaces.AdapterPositionInterface
import com.example.test.Adapter.DriverAdapter
import com.example.test.LiveDataProjektu.ViewModelSystemuDyspozycji
import com.example.test.R
import kotlinx.android.synthetic.main.fragment_admin_vehicle_list.*


class AdminDriverList : Fragment(), AdapterPositionInterface {
    private lateinit var viewModel: ViewModelSystemuDyspozycji

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_driver_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelSystemuDyspozycji::class.java)
        rcl_AdminVehicleRecycler.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
        rcl_AdminVehicleRecycler.adapter = DriverAdapter(viewModel.adminDriversList,this)


    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminDriverList().apply {

            }
    }

    override fun onDriverClicked(position: Int) {
        viewModel.driverAdapterPosition.postValue(position)

        //Toast.makeText(requireContext(),"${viewModel.adapterPositionViewModel.value}",Toast.LENGTH_SHORT).show()
        //Navigation.findNavController(requireView()).navigate(R.id.driverSelected2)
    }

    override fun onVehicleClicked(position: Int) {
        viewModel.vehicleAdapterPosition.postValue(position)
    }
}