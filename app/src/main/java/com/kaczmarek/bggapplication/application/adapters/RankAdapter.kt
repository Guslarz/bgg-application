package com.kaczmarek.bggapplication.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.databinding.RankRowBinding
import com.kaczmarek.bggapplication.entities.database.Rank
import java.time.format.DateTimeFormatter

class RankAdapter(private val ranks: List<Rank>) : RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    class ViewHolder(val binding: RankRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RankRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rank = ranks[position]

        holder.binding.textViewDate.text = rank.datetime.format(dateFormatter)

        holder.binding.textViewValue.text = rank.value.toString().padStart(8)
    }

    override fun getItemCount(): Int = ranks.size
}