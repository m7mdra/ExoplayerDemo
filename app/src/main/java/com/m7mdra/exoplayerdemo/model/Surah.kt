package com.m7mdra.exoplayerdemo.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Surah(
    @SerializedName("ayahs")
    val ayahs: List<Ayah>,
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: Int,

    ) : Parcelable