package com.weminder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weminder.data.Group
import com.weminder.data.Task
import com.weminder.data.User

@Database(entities = [User::class, Group::class, Task::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao?
    abstract fun groupDao(): GroupDao?
    abstract fun taskDao(): TaskDao?

    companion object {
        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (db == null) {
                synchronized(AppDatabase::class) {
                    db = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "weminder.db"
                    ).build()
                }
            }
            return db
        }
    }
}