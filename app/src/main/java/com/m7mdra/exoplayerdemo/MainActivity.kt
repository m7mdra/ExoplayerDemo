package com.m7mdra.exoplayerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m7mdra.exoplayerdemo.model.Surah
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    private var surahList = mutableListOf<Surah>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
    }

    private fun loadData() {
        val readText = assets.open("data.json").reader().readText()
        val listType: Type = object : TypeToken<List<Surah>>() {}.type
        val list: List<Surah> = Gson().fromJson(readText, listType)
        surahList.addAll(list)
    }


}