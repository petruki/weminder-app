package com.weminder.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weminder.data.Group
import com.weminder.data.Log
import com.weminder.data.Task
import com.weminder.db.AppDatabase
import com.weminder.utils.MOCK_TASKS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    var selected : MutableLiveData<Task> = MutableLiveData<Task>()
    var taskLogs : MutableLiveData<List<Log>> = MutableLiveData<List<Log>>()

    fun getAllTaskByGroupId(groupId: String) = database?.taskDao()?.getGroupTasks(groupId)

    fun insert(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.taskDao()?.insert(task)
            }
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.taskDao()?.update(task)
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.taskDao()?.delete(task)
            }
        }
    }

    fun selectTask(taskId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                selected.postValue(database?.taskDao()?.getTask(taskId))
            }
        }
    }
}