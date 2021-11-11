package com.weminder.db

import androidx.room.*
import com.weminder.data.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun getUsersByIds(userIds: List<String>): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user")
    fun deleteAll(): Int

}