package com.kaczmarek.bggapplication.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.databinding.ArtistRowBinding
import com.kaczmarek.bggapplication.entities.database.Artist

class ArtistAdapter(private val artists: List<Artist>) :
    RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {

    private var onDeleteListener: (Artist) -> Unit = {}

    data class ViewHolder(val binding: ArtistRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ArtistRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = artists[position]

        holder.binding.textViewName.text = artist.name

        holder.binding.imageButtonDelete.setOnClickListener { onDeleteListener(artist) }
    }

    override fun getItemCount(): Int = artists.size

    fun setOnDeleteListener(listener: (Artist) -> Unit) {
        onDeleteListener = listener
    }
}