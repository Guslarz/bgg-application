package com.kaczmarek.bggapplication.application.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.adapters.CollectionItemAdapter
import com.kaczmarek.bggapplication.application.viewmodels.BggUserCollectionViewModel
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.databinding.ActivityUserCollectionBinding
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameCollectionItem

class UserCollectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserCollectionBinding
    private val viewModel: BggUserCollectionViewModel by viewModels {
        BggViewModelFactory((application as BggApplication).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSearch.setOnClickListener { onSearchClick() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CollectionItemAdapter(listOf())

        binding.buttonRanking.setOnClickListener { onRankingClick() }

        binding.buttonBack.setOnClickListener { onBackClick() }

        viewModel.getErrorMessage().observe(this, this::onErrorMessage)
        viewModel.getIsLoading().observe(this, this::onLoading)
        viewModel.getHasCollection().observe(this, this::onHasCollection)
        viewModel.getUserCollection().observe(this, this::onUserCollection)
    }

    private fun onSearchClick() {
        val username = binding.editTextUsername.text.toString()
        viewModel.searchByUsername(username)
    }

    private fun onItemImport(item: BggBoardGameCollectionItem) {
        viewModel.importItem(item)
    }

    private fun onRankingClick() {
        viewModel.updateRanking()
    }

    private fun onBackClick() {
        finish()
    }

    private fun onErrorMessage(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    private fun onLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun onHasCollection(hasCollection: Boolean) {
        binding.buttonRanking.isEnabled = hasCollection
    }

    private fun onUserCollection(userCollection: List<BggBoardGameCollectionItem>) {
        val adapter = CollectionItemAdapter(userCollection)
        adapter.setOnAddItemListener(this::onItemImport)
        binding.recyclerView.adapter = adapter
    }
}