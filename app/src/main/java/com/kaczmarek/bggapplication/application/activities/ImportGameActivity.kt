package com.kaczmarek.bggapplication.application.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.adapters.BggOverviewAdapter
import com.kaczmarek.bggapplication.application.viewmodels.BggBoardGameSearchViewModel
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.databinding.ActivityImportGameBinding
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview

class ImportGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImportGameBinding
    private val viewModel: BggBoardGameSearchViewModel by viewModels {
        BggViewModelFactory((application as BggApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getIsLoading().observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = BggOverviewAdapter(listOf())
        viewModel.getOverviewList().observe(this, {
            val adapter = BggOverviewAdapter(it)
            adapter.setOnSelectListener(this::onOverviewSelect)
            binding.recyclerView.adapter = adapter
        })

        binding.buttonSearch.setOnClickListener { onSearchClick() }
        binding.buttonAddCustom.setOnClickListener { onCustomAddClick() }
    }

    private fun onSearchClick() {
        val name = binding.editTextName.text.toString()
        viewModel.searchByName(name)
    }

    private fun onOverviewSelect(overview: BggBoardGameOverview) {
        val intent = Intent(this, InsertBoardGameActivity::class.java).apply {
            putExtra(InsertBoardGameActivity.PARCEL_NAME, overview)
        }
        startActivity(intent)
    }

    private fun onCustomAddClick() {
        val intent = Intent(this, InsertBoardGameActivity::class.java)
        startActivity(intent)
    }
}