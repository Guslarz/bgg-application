package com.kaczmarek.bggapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.kaczmarek.bggapplication.databinding.ActivityMainBinding
import com.kaczmarek.bggapplication.entities.external.BggApiResponse
import com.kaczmarek.bggapplication.logic.bggapi.BggApiDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}