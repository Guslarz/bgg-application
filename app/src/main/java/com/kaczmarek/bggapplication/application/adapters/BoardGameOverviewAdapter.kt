package com.kaczmarek.bggapplication.application.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.databinding.BoardGameOverviewRowBinding
import com.kaczmarek.bggapplication.entities.database.BoardGameOverview
import com.squareup.picasso.Picasso

class BoardGameOverviewAdapter(private val data: List<BoardGameOverview>) :
    RecyclerView.Adapter<BoardGameOverviewAdapter.ViewHolder>() {

    private var onDetailsListener: (BoardGameOverview) -> Unit = {}
    private var onDeleteListener: (BoardGameOverview) -> Unit = {}

    class ViewHolder(val binding: BoardGameOverviewRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BoardGameOverviewRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val overview = data[position]
        val context = holder.itemView.context

        Picasso.get().load(overview.thumbnail)
            .error(ColorDrawable(Color.BLACK))
            .resize(200, 200)
            .into(holder.binding.imageViewThumbnail)
        holder.binding.imageViewThumbnail.setOnClickListener { onDetailsListener(overview) }

        holder.binding.textViewTitle.text = String.format(
            context.getString(R.string.board_game_title_and_year_template),
            overview.title, overview.yearPublished
        )
        holder.binding.textViewTitle.setOnClickListener { onDetailsListener(overview) }

        val rankTemplate = holder.itemView.context.getString(R.string.rank_template)
        holder.binding.textViewRank.text = String.format(
            rankTemplate, overview.rank?.toString() ?: "-")

        holder.binding.buttonDelete.setOnClickListener { onDeleteListener(overview) }
    }

    override fun getItemCount(): Int = data.size

    fun setOnDetailsListener(listener: (BoardGameOverview) -> Unit) {
        onDetailsListener = listener
    }

    fun setOnDeleteListener(listener: (BoardGameOverview) -> Unit) {
        onDeleteListener = listener
    }
}