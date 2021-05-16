package com.kaczmarek.bggapplication.application.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.adapters.BoardGameOverviewAdapter
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.application.viewmodels.BoardGameOverviewListViewModel
import com.kaczmarek.bggapplication.databinding.ActivityMainBinding
import com.kaczmarek.bggapplication.entities.BoardGameOverviewOrder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: BoardGameOverviewListViewModel by viewModels {
        BggViewModelFactory((application as BggApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getBoardGameOverviewList(BoardGameOverviewOrder.RANK_ASC).observe(this) {
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = BoardGameOverviewAdapter(it)
        }
    }
}