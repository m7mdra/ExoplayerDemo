package com.m7mdra.exoplayerdemo.model


import com.google.gson.annotations.SerializedName

data class Ayah(
    @SerializedName("audio")
    val audio: String,



    @SerializedName("number")
    val number: Int,
    @SerializedName("numberInSurah")
    val numberInSurah: Int,
    @SerializedName("page")
    val page: Int,

    @SerializedName("text")
    val text: String
)