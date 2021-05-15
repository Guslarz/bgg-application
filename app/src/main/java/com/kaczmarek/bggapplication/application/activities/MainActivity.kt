package com.kaczmarek.bggapplication.application.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kaczmarek.bggapplication.application.BggApplication
import com.kaczmarek.bggapplication.application.viewmodels.BggViewModelFactory
import com.kaczmarek.bggapplication.application.viewmodels.BoardGameOverviewListViewModel
import com.kaczmarek.bggapplication.databinding.ActivityMainBinding
import com.kaczmarek.bggapplication.entities.BoardGameOverviewOrder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: BoardGameOverviewListViewModel by viewModels {
            BggViewModelFactory((application as BggApplication).database)
        }

        viewModel.getBoardGameOverviewList(BoardGameOverviewOrder.RANK_ASC)
            .observe(this) { list ->
                binding.textView.text = list.size.toString()
        }

        /*
        val bggApiDao = BggApiDao()
        lifecycleScope.launch{
            binding.textView.text = "Loading..."
            val response = bggApiDao.getGameOverviewsByName("kawerna")
            if (response is BggApiResponse.Success) {
                binding.textView.text = response.data[0].title
            } else if (response is BggApiResponse.Error) {
                binding.textView.text = response.exception.toString()
            }
        }

        lifecycleScope.launch{
            binding.textView2.text = "Loading..."
            val response2 = bggApiDao.getGameDetails(102794)

            if (response2 is BggApiResponse.Success) {
                binding.textView2.text = response2.data.title
            } else if (response2 is BggApiResponse.Error) {
                binding.textView2.text = response2.exception.toString()
            }
        }

        lifecycleScope.launch{
            binding.textView3.text = "Loading..."
            val response3 = bggApiDao.getCollectionOfUser("PolskiMatHandel")

            if (response3 is BggApiResponse.Success) {
                binding.textView3.text = response3.data[0].title
            } else if (response3 is BggApiResponse.Error) {
                binding.textView3.text = response3.exception.toString()
            }
        }
         */
    }
}