package com.weminder.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weminder.data.Log
import com.weminder.data.User
import java.lang.reflect.Type

class Converter {

    @TypeConverter
    fun fromUserList(users: List<String>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<User>>() {}.type
        return gson.toJson(users, type)
    }

    @TypeConverter
    fun toUserList(users: String): List<String> {
        val gson = Gson()
        val type =
            object : TypeToken<List<String>>() {}.type
        return gson.fromJson(users, type)
    }

    @TypeConverter
    fun fromLogList(log: List<Log>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Log>>() {}.type
        return gson.toJson(log, type)
    }

    @TypeConverter
    fun toLogList(log: String): List<Log> {
        val gson = Gson()
        val type =
            object : TypeToken<List<Log>>() {}.type
        return gson.fromJson(log, type)
    }

}