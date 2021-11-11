package com.weminder.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Log(
    var username: String,
    var message: String,

    @SerializedName("created_at")
    var createdAt: String
) : Parcelable