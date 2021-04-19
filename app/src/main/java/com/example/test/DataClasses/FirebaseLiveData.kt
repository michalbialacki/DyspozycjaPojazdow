package com.example.test.DataClasses

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.firebase.database.*

class FirebaseLiveData (context: Context,sciezka : DatabaseReference) : LiveData<PobranaWartosc>(){
    private var database = FirebaseDatabase.getInstance()
    private var dostep = sciezka
    private var pobranaPozycjaFirebase = PobranaWartosc("")


    companion object{
        private val TAG = FirebaseLiveData::class.java.name
    }


    override fun onActive() {
        super.onActive()
        dostep.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var zmienna = PobranaWartosc(snapshot.getValue().toString())
                pobranaPozycjaFirebase = zmienna
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}

data class PobranaWartosc(var wartosc: String)