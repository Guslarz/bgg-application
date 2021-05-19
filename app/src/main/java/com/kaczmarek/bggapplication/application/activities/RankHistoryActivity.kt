package com.kaczmarek.bggapplication.application.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.adapters.RankAdapter
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.application.viewmodels.BoardGameRankingViewModel
import com.kaczmarek.bggapplication.databinding.ActivityRankHistoryBinding
import com.kaczmarek.bggapplication.entities.database.Rank

class RankHistoryActivity : AppCompatActivity() {

    companion object {
        const val BGG_ID_EXTRA_NAME = "bggId"
        const val TITLE_EXTRA_NAME = "title"
    }

    private lateinit var binding: ActivityRankHistoryBinding
    private val viewModel: BoardGameRankingViewModel by viewModels {
        BggViewModelFactory((application as BggApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bggId = intent.getLongExtra(BGG_ID_EXTRA_NAME, -1)
        val title = intent.getStringExtra(TITLE_EXTRA_NAME)!!

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = RankAdapter(listOf())

        binding.textViewItemTitle.text = title

        binding.buttonBack.setOnClickListener { onBackClick() }

        viewModel.getBoardGameRanking().observe(this, this::onBoardGameRanking)

        viewModel.loadTarget(bggId)
    }

    private fun onBackClick() {
        finish()
    }

    private fun onBoardGameRanking(ranking: List<Rank>) {
        binding.recyclerView.adapter = RankAdapter(ranking)
    }
}