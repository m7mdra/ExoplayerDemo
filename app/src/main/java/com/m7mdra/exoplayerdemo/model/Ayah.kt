package com.m7mdra.exoplayerdemo.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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
):Parcelable