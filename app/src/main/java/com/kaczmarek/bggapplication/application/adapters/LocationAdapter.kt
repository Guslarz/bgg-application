package com.kaczmarek.bggapplication.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.databinding.LocationRowBinding
import com.kaczmarek.bggapplication.entities.database.LocationWithBoardGameCount

class LocationAdapter(private val locations: List<LocationWithBoardGameCount>) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    private var onLocationDetailsListener: (LocationWithBoardGameCount) -> Unit = {}
    private var onLocationDeleteListener: (LocationWithBoardGameCount) -> Unit = {}

    class ViewHolder(val binding: LocationRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LocationRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]

        holder.binding.content.setOnClickListener { onLocationDetailsListener(location) }

        holder.binding.textViewName.text = location.location.name

        val template = holder.itemView.context.getString(R.string.location_board_game_count_template)
        holder.binding.textViewCount.text = String.format(template, location.boardGameCount)

        holder.binding.buttonDelete.setOnClickListener { onLocationDeleteListener(location) }
    }

    override fun getItemCount(): Int = locations.size

    fun setOnLocationDetailsListener(listener: (LocationWithBoardGameCount) -> Unit) {
        onLocationDetailsListener = listener
    }

    fun setOnLocationDeleteListener(listener: (LocationWithBoardGameCount) -> Unit) {
        onLocationDeleteListener = listener
    }
}