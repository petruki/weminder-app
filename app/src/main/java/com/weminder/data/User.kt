package com.weminder.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    var id: String,
    var username: String,
    var password: String,
    var email: String
) : Parcelable