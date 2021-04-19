package com.example.test.LiveDataProjektu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.test.DataClasses.LocationLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    fun getLocationData() = locationData
}