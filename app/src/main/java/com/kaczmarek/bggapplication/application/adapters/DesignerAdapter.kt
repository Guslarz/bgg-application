package com.kaczmarek.bggapplication.application.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaczmarek.bggapplication.databinding.DesignerRowBinding
import com.kaczmarek.bggapplication.entities.database.Designer

class DesignerAdapter(private val designers: List<Designer>) :
    RecyclerView.Adapter<DesignerAdapter.ViewHolder>() {

    private var onDeleteListener: (Designer) -> Unit = {}

    data class ViewHolder(val binding: DesignerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DesignerRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val designer = designers[position]

        holder.binding.textViewName.text = designer.name

        holder.binding.imageButtonDelete.setOnClickListener { onDeleteListener(designer) }
    }

    override fun getItemCount(): Int = 0

    fun setOnDeleteListener(listener: (Designer) -> Unit) {
        onDeleteListener = listener
    }
}