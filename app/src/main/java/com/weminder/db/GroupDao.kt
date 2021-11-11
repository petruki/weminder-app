package com.weminder.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.weminder.data.Group

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: Group)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(group: Group)

    @Delete
    fun delete(group: Group)

    @Query("DELETE FROM `group`")
    fun deleteAll(): Int

    @Query("SELECT * FROM `group` WHERE id = :groupId")
    fun getGroup(groupId: String): Group

    @Query("SELECT * FROM `group`")
    fun getAllGroups(): List<Group>

}