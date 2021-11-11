package com.weminder.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weminder.data.Group
import com.weminder.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchGroupViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)

    val groups: MutableLiveData<List<Group>> = MutableLiveData<List<Group>>()
    val selected: MutableLiveData<Group> = MutableLiveData<Group>()

    fun selectGroupById(groupId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                selected.postValue(database?.groupDao()?.getGroup(groupId))
            }
        }
    }
}