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

    fun syncAllGroups(remoteGroups: List<Group>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groups.postValue(remoteGroups)

                // Sync existing groups
                val localGroups = database?.groupDao()?.getAllGroups()
                remoteGroups.forEach { remote ->
                    var new = true
                    localGroups?.forEach { local ->
                        if (local.id == remote.id) {
                            new = false
                            if (local != remote) database?.groupDao()?.update(remote)
                        }
                    }
                    if (new) database?.groupDao()?.insert(remote)
                }
            }
        }
    }

    fun syncAllGroupTasks(remoteTasks: List<Task>, groupId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groupTasks.postValue(remoteTasks)

                // Sync existing tasks
                val localGroupTasks = database?.taskDao()?.getGroupTasks(groupId)
                remoteTasks.forEach { remote ->
                    var new = true
                    localGroupTasks?.forEach { local ->
                        if (local.id == remote.id) {
                            new = false
                            if (local != remote) database?.taskDao()?.update(remote)
                        }
                    }
                    if (new) database?.taskDao()?.insert(remote)
                }

                // Sync deleted tasks
                localGroupTasks?.forEach { local ->
                    var deleted = true
                    remoteTasks.forEach { remote ->
                        if (local.id == remote.id)
                            deleted = false
                    }
                    if (deleted) database?.taskDao()?.delete(local)
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