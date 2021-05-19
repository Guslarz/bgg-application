package com.kaczmarek.bggapplication.application.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kaczmarek.bggapplication.databinding.ActivityRankHistoryBinding

class RankHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}