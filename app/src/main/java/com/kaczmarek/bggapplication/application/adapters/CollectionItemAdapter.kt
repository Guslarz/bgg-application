package com.kaczmarek.bggapplication.application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.databinding.CollectionItemRowBinding
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameCollectionItem

class CollectionItemAdapter(private val items: List<BggBoardGameCollectionItem>) :
    RecyclerView.Adapter<CollectionItemAdapter.ViewHolder>() {

    private var onAddItemListener: (BggBoardGameCollectionItem) -> Unit = {}

    class ViewHolder(val binding: CollectionItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CollectionItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        val titleTemplate = context.getString(R.string.board_game_title_and_year_template)
        holder.binding.textViewItemTitle.text =
            String.format(titleTemplate, item.title, item.yearPublished)

        if (item.comment != null) {
            val commentTemplate = "\"%s\""
            holder.binding.textViewItemComment.text = String.format(commentTemplate, item.comment)
        }

        holder.binding.buttonAddItem.setOnClickListener {
            onAddItemListener(item)
            it.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size

    fun setOnAddItemListener(listener: (BggBoardGameCollectionItem) -> Unit) {
        onAddItemListener = listener
    }
}