package com.example.test

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    private var backPressedTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


   /* override fun onBackPressed() {
        if (backPressedTime + 3000 >System.currentTimeMillis()){
            super.onBackPressed()
            findNavController(R.id.fragment).navigate(R.id.logowanie)
        }
        else {
            Toast.makeText(applicationContext,"Ponowne naciśnięcie przycisku powrotu spowoduje wylogowanie",Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()


    }*/
}

