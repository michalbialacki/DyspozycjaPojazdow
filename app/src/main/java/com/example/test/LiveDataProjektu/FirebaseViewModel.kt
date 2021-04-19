package com.example.test.LiveDataProjektu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.test.DataClasses.FirebaseLiveData
import com.google.firebase.database.DatabaseReference

class FirebaseViewModel (application: Application, sciezka : DatabaseReference) : AndroidViewModel(application) {
    private val fireBaseData = FirebaseLiveData(application,sciezka)

    fun getFirebaseData() = fireBaseData

}