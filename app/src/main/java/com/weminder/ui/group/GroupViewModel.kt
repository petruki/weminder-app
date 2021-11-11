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
    var selected : MutableLiveData<Group> = MutableLiveData<Group>()
    var groupTasks : MutableLiveData<List<Task>> = MutableLiveData<List<Task>>()

    fun getAllGroups() = database?.groupDao()?.getAllGroups()

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