package com.m7mdra.exoplayerdemo.model


import com.google.gson.annotations.SerializedName

data class Surah(
    @SerializedName("ayahs")
    val ayahs: List<Ayah>,
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: Int,

)