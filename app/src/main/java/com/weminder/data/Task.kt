package com.weminder.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task")
data class Task(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var content: String = "",
    var status: String = "",
    var log: List<Log> = emptyList(),

    @SerializedName("user_id")
    var createdBy: String,

    @SerializedName("group_id")
    var groupId: String
) : Parcelable {
    constructor(createdBy: String, groupId: String):
        this(createdBy=createdBy, groupId=groupId, status="Opened")
}