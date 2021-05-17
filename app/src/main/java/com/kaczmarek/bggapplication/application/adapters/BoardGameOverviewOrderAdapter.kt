package com.kaczmarek.bggapplication.application.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.BoardGameOverviewOrder

class BoardGameOverviewOrderAdapter(context: Context) :
    ArrayAdapter<BoardGameOverviewOrder>(context, android.R.layout.simple_spinner_item,
        BoardGameOverviewOrder.values()) {

    companion object {
        private val labelIds = mapOf(
            BoardGameOverviewOrder.RANK_ASC to R.string.rank_asc,
            BoardGameOverviewOrder.RANK_DESC to R.string.rank_desc,
            BoardGameOverviewOrder.YEAR_ASC to R.string.year_asc,
            BoardGameOverviewOrder.YEAR_DESC to R.string.year_desc
        )
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = context.getString(labelIds[getItem(position)]!!)
        val view = (convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item,
                parent, false)) as CheckedTextView

        view.text = label
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = context.getString(labelIds[getItem(position)]!!)
        val view = (convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item,
                parent, false)) as CheckedTextView

        view.text = label
        return view
    }
}