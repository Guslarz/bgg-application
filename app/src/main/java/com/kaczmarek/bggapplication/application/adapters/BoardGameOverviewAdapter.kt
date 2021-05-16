package com.kaczmarek.bggapplication.application.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.databinding.BoardGameOverviewRowBinding
import com.kaczmarek.bggapplication.entities.database.BoardGameOverview
import com.squareup.picasso.Picasso

class BoardGameOverviewAdapter(private val data: List<BoardGameOverview>) :
    RecyclerView.Adapter<BoardGameOverviewAdapter.ViewHolder>() {

    private var onDeleteListener: (BoardGameOverview) -> Unit = {}

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = BoardGameOverviewRowBinding.inflate(LayoutInflater.from(itemView.context))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_game_overview_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val overview = data[position]
        val context = holder.itemView.context

        holder.binding.textViewRank.text = overview.rank?.toString() ?: "-"

        Picasso.get().load(overview.thumbnail)
            .error(ColorDrawable(Color.BLACK))
            .resize(200, 200)
            .into(holder.binding.imageViewThumbnail)

        holder.binding.textViewContentTitle.text = String.format(
            context.getString(R.string.board_game_overview_title_and_year_template),
            overview.title, overview.yearPublished)

        holder.binding.textViewContentDescription.text = overview.description

        holder.binding.buttonDelete.setOnClickListener { onDeleteListener(overview) }
    }

    override fun getItemCount(): Int = data.size

    fun setOnDeleteListener(listener: (BoardGameOverview) -> Unit) {
        onDeleteListener = listener
    }
}