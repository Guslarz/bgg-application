package com.kaczmarek.bggapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kaczmarek.bggapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    }
}