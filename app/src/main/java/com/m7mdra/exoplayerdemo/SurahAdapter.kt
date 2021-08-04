package com.m7mdra.exoplayerdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m7mdra.exoplayerdemo.model.Surah

class SurahAdapter(
    private val list: List<Surah>,
    private val onClickListener: (Surah) -> Unit = {}
) : RecyclerView.Adapter<SurahViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        return SurahViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_surah, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        val surah = list[position]
        holder.rootView.setOnClickListener {
            onClickListener.invoke(surah)
        }
        holder.ayahCountTextView.text = "${surah.ayahs.size} سورة"
        holder.surahNameTextView.text = surah.name
        holder.surahNumberTextView.text = "${surah.number}"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}