package com.kaczmarek.bggapplication.application.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.adapters.BoardGameOverviewAdapter
import com.kaczmarek.bggapplication.application.adapters.BoardGameOverviewOrderAdapter
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.application.viewmodels.BoardGameOverviewListViewModel
import com.kaczmarek.bggapplication.databinding.ActivityMainBinding
import com.kaczmarek.bggapplication.entities.BoardGameOverviewOrder
import com.kaczmarek.bggapplication.entities.database.BoardGameOverview

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: BoardGameOverviewListViewModel by viewModels {
        BggViewModelFactory((application as BggApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = BoardGameOverviewAdapter(listOf())
        viewModel.getBoardGameOverviewList().observe(this) {
            val adapter = BoardGameOverviewAdapter(it)
            adapter.setOnDetailsListener(this::onBoardGameDetails)
            adapter.setOnDeleteListener(this::onBoardGameDelete)
            binding.recyclerView.adapter = adapter
        }

        viewModel.getErrorMessage().observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loadBoardGameOverviewList(BoardGameOverviewOrder.values()[0])

        val spinnerAdapter = BoardGameOverviewOrderAdapter(this)
        binding.spinnerOrderBy.adapter = spinnerAdapter
        binding.spinnerOrderBy.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?,
                position: Int, id: Long
            ) {

                viewModel.loadBoardGameOverviewList(spinnerAdapter.getItem(position)!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.buttonAdd.setOnClickListener { onAddClick() }
        binding.buttonUserCollection.setOnClickListener { onUserCollectionClick() }
        binding.buttonLocations.setOnClickListener { onLocationsClick() }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.reloadBoardGameOverviewList()
    }

    private fun onBoardGameDetails(overview: BoardGameOverview) {
        val intent = Intent(this, UpdateBoardGameActivity::class.java).apply {
            putExtra(UpdateBoardGameActivity.EXTRA_NAME, overview.id)
        }
        startActivity(intent)
    }

    private fun onBoardGameDelete(overview: BoardGameOverview) {
        viewModel.deleteBoardGame(overview)
    }

    private fun onAddClick() {
        val intent = Intent(this, ImportGameActivity::class.java)
        startActivity(intent)
    }

    private fun onUserCollectionClick() {
        val intent = Intent(this, UserCollectionActivity::class.java)
        startActivity(intent)
    }

    private fun onLocationsClick() {
        val intent = Intent(this, LocationListActivity::class.java)
        startActivity(intent)
    }
}