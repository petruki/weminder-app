package com.weminder.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "group")
data class Group(
    @SerializedName("_id")
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var alias: String = "",
    var users: List<String>
) : Parcelable {
    constructor():
        this(users = emptyList())

    constructor(name: String, alias: String):
        this("",name,alias, emptyList())
}