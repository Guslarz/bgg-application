package com.kaczmarek.bggapplication.application.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.adapters.LocationAdapter
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.application.viewmodels.LocationListViewModel
import com.kaczmarek.bggapplication.databinding.ActivityLocationListBinding
import com.kaczmarek.bggapplication.entities.database.Location
import com.kaczmarek.bggapplication.entities.database.LocationWithBoardGameCount

class LocationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationListBinding
    private val viewModel: LocationListViewModel by viewModels {
        BggViewModelFactory((application as BggApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = LocationAdapter(listOf())
        viewModel.getLocationList().observe(this, {
            val adapter = LocationAdapter(it)
            adapter.setOnLocationDetailsListener(this::onLocationDetails)
            adapter.setOnLocationDeleteListener(this::onLocationDelete)
            binding.recyclerView.adapter = adapter
        })

        viewModel.getErrorMessage().observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        binding.buttonAdd.setOnClickListener { onButtonAddClick() }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.refresh()
    }

    private fun onLocationDetails(location: LocationWithBoardGameCount) {
        val intent = Intent(this, LocationDetailsActivity::class.java).apply {
            putExtra(LocationDetailsActivity.PARCEL_NAME, location.location)
        }
        startActivity(intent)
    }

    private fun onLocationDelete(location: LocationWithBoardGameCount) {
        Log.i("LOCATION", "DELETE")
        viewModel.removeLocation(location)
    }

    private fun onButtonAddClick() {
        viewModel.addLocation(Location(0, binding.editTextName.text.toString()))
    }
}