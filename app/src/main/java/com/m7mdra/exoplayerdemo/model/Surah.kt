package com.m7mdra.exoplayerdemo.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Surah(

    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfAyahs")
    val ayatCount:Int
    ) : Parcelable{
        val audio:String get() {
            return  String.format("https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee/%03d.mp3",number)
        }
    }