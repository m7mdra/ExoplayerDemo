package com.m7mdra.exoplayerdemo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_surah.view.*

class SurahViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val surahNameTextView: TextView = view.surahNameTextView
    val surahNumberTextView: TextView = view.surahNumberTextView
    val ayahCountTextView: TextView = view.ayahCountTextView
    val playButton: ImageView = view.playButton
    val rootView = view.rootView
}