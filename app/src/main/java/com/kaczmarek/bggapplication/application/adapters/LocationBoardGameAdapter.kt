package com.kaczmarek.bggapplication.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.databinding.LocationBoardGameRowBinding
import com.kaczmarek.bggapplication.entities.database.LocationBoardGameOverview

class LocationBoardGameAdapter(private val boardGames: List<LocationBoardGameOverview>) :
    RecyclerView.Adapter<LocationBoardGameAdapter.ViewHolder>() {

    class ViewHolder(val binding: LocationBoardGameRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LocationBoardGameRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = boardGames[position]

        val template =
            holder.itemView.context.getString(R.string.board_game_title_and_year_template)
        holder.binding.textViewItemTitle.text = String.format(
            template, game.boardGame.title,
            game.boardGame.yearPublished
        )

        holder.binding.textViewComment.text = game.locationRelation.comment
    }

    override fun getItemCount(): Int = boardGames.size
}