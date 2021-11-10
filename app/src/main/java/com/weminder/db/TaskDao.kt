package com.weminder.db

import androidx.room.*
import com.weminder.data.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM `task` WHERE id = :taskId")
    fun getTask(taskId: String): Task

    @Query("SELECT * FROM `task` WHERE groupId = :groupId")
    fun getGroupTasks(groupId: String): List<Task>

}