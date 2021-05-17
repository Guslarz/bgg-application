package com.kaczmarek.bggapplication.application.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.adapters.LocationBoardGameAdapter
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.application.viewmodels.LocationUpdateViewModel
import com.kaczmarek.bggapplication.databinding.ActivityLocationDetailsBinding
import com.kaczmarek.bggapplication.entities.database.Location

class LocationDetailsActivity : AppCompatActivity() {

    companion object {
        const val PARCEL_NAME = "location"
    }

    private lateinit var binding: ActivityLocationDetailsBinding
    private val viewModel: LocationUpdateViewModel by viewModels {
        BggViewModelFactory((application as BggApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val location: Location = intent.getParcelableExtra(PARCEL_NAME)!!

        viewModel.getErrorMessage().observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = LocationBoardGameAdapter(listOf())
        viewModel.getLocationBoardGames().observe(this, {
            val adapter = LocationBoardGameAdapter(it)
            binding.recyclerView.adapter = adapter
        })
        viewModel.loadTarget(location)

        binding.editTextName.setText(location.name, TextView.BufferType.EDITABLE)

        binding.buttonSave.setOnClickListener { onSaveClick() }
        binding.buttonCancel.setOnClickListener { onCancelClick() }
    }

    private fun onSaveClick() {
        viewModel.getLocation().name = binding.editTextName.text.toString()
        viewModel.commit {
            finish()
        }
    }

    private fun onCancelClick() {
        finish()
    }
}