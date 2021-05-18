package com.kaczmarek.bggapplication.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.databinding.BggOverviewRowBinding
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview
import com.squareup.picasso.Picasso

class BggOverviewAdapter(private val overviews: List<BggBoardGameOverview>) :
    RecyclerView.Adapter<BggOverviewAdapter.ViewHolder>() {

    private var onSelectListener: (BggBoardGameOverview) -> Unit = {}

    class ViewHolder(val binding: BggOverviewRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BggOverviewRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val overview = overviews[position]

        holder.itemView.setOnClickListener { onSelectListener(overview) }

        val template = holder.itemView.context.getString(R.string.board_game_title_and_year_template)
        holder.binding.textViewOverviewTitle.text =
            String.format(template, overview.title, overview.yearPublished)
    }

    override fun getItemCount(): Int = overviews.size

    fun setOnSelectListener(listener: (BggBoardGameOverview) -> Unit) {
        onSelectListener = listener
    }
}