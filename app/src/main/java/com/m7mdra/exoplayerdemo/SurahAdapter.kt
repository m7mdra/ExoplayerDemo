package com.m7mdra.exoplayerdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m7mdra.exoplayerdemo.model.Surah

class SurahAdapter(private val list: List<Surah>) : RecyclerView.Adapter<SurahViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        return SurahViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_surah, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        val (ayahs, name, number) = list[position]
        holder.ayahCountTextView.text = "${ayahs.size} سورة"
        holder.surahNameTextView.text = name
        holder.surahNumberTextView.text = "$number"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}