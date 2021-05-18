package com.kaczmarek.bggapplication.application.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView

class DefaultValueSpinnerAdapter<T>(
    context: Context,
    data: List<T>,
    private val defaultLabel: String
) :
    ArrayAdapter<T?>(
        context,
        android.R.layout.simple_spinner_dropdown_item,
        listOf(null).plus(data)
    ) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            val view = (convertView ?: LayoutInflater.from(context)
                .inflate(
                    android.R.layout.simple_spinner_dropdown_item,
                    parent, false
                )) as CheckedTextView
            view.text = defaultLabel
            view
        } else {
            super.getView(position, convertView, parent)
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            val view = (convertView ?: LayoutInflater.from(context)
                .inflate(
                    android.R.layout.simple_spinner_dropdown_item,
                    parent, false
                )) as CheckedTextView
            view.text = defaultLabel
            view
        } else {
            super.getView(position, convertView, parent)
        }
    }
}