package com.weminder.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "group")
data class Group(
    @PrimaryKey
    var id: String,
    var name: String,
    var alias: String,
    var users: List<User>
) : Parcelable {
    constructor(name: String, alias: String):
        this("", name, alias, emptyList())
}