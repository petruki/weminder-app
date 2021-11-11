package com.weminder.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @SerializedName("_id")
    @PrimaryKey
    var id: String = "",
    var username: String = "",
    var password: String = "",
    var email: String = ""
) : Parcelable {
    constructor(username: String, password: String):
        this(id="",username = username, password = password, email="")
}