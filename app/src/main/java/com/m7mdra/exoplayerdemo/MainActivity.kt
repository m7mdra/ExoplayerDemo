package com.m7mdra.exoplayerdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m7mdra.exoplayerdemo.model.Surah
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    private var surahList = mutableListOf<Surah>()
    private lateinit var adapter: SurahAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = SurahAdapter(surahList, onClickListener = { surah ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("surah", surah)
            startActivity(intent)
        })
        loadData()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun loadData() {
        val readText = assets.open("data.json").reader().readText()
        val listType: Type = object : TypeToken<List<Surah>>() {}.type
        val list: List<Surah> = Gson().fromJson(readText, listType)
        surahList.addAll(list)

        adapter.notifyDataSetChanged()
    }


}