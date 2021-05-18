package com.kaczmarek.bggapplication.application.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.database.BoardGameType

class BoardGameTypeAdapter(context: Context) :
    ArrayAdapter<BoardGameType>(
        context, android.R.layout.simple_spinner_item,
        BoardGameType.values()
    ) {

    companion object {
        private val LABEL_IDS = mapOf(
            BoardGameType.GAME to R.string.game,
            BoardGameType.EXPANSION to R.string.expansion,
            BoardGameType.MIXED to R.string.mixed
        )
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = context.getString(LABEL_IDS[getItem(position)]!!)
        val view = (convertView ?: LayoutInflater.from(context)
            .inflate(
                android.R.layout.simple_spinner_dropdown_item,
                parent, false
            )) as CheckedTextView

        view.text = label
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = context.getString(LABEL_IDS[getItem(position)]!!)
        val view = (convertView ?: LayoutInflater.from(context)
            .inflate(
                android.R.layout.simple_spinner_dropdown_item,
                parent, false
            )) as CheckedTextView

        view.text = label
        return view
    }
}