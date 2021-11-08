package com.weminder.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weminder.data.Group
import com.weminder.utils.MOCK_GROUPS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchGroupViewModel : ViewModel() {

    private val mockGroups: List<Group> = MOCK_GROUPS
    val groups: MutableLiveData<List<Group>> = MutableLiveData<List<Group>>()

    fun findGroupsByAlias(alias: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groups.postValue(mockGroups.filter { it.alias.lowercase() == alias.lowercase() })
            }
        }
    }
}