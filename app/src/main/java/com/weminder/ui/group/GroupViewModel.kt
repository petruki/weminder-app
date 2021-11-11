package com.weminder.ui.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weminder.data.Group
import com.weminder.data.Task
import com.weminder.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    var selected: MutableLiveData<Group> = MutableLiveData<Group>()
    var groups: MutableLiveData<List<Group>> = MutableLiveData<List<Group>>()
    var groupTasks: MutableLiveData<List<Task>> = MutableLiveData<List<Task>>()

    fun getAllGroups() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groups.postValue(database?.groupDao()?.getAllGroups())
            }
        }
    }

    fun syncAllGroups(sync: List<Group>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groups.postValue(sync)

                val allGroups = database?.groupDao()?.getAllGroups()
                sync.forEach {
                    var new = true
                    allGroups?.forEach { g ->
                        if (g.id == it.id) {
                            new = false
                            if (g != it) database?.groupDao()?.update(it)
                        }
                    }
                    if (new) database?.groupDao()?.insert(it)
                }
            }
        }
    }

    fun insert(group: Group) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.groupDao()?.insert(group)
            }
        }
    }

    fun update(group: Group) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.groupDao()?.update(group)
                selected.postValue(group)
            }
        }
    }

    fun delete(group: Group) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.groupDao()?.delete(group)
                database?.taskDao()?.deleteByGroupId(group.id)
            }
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.taskDao()?.insert(task)
            }
        }
    }

    fun selectGroup(groupId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                selected.postValue(database?.groupDao()?.getGroup(groupId))
                groupTasks.postValue(database?.taskDao()?.getGroupTasks(groupId))
            }
        }
    }
}