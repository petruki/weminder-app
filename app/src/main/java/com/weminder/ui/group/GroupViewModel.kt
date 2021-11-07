package com.weminder.ui.group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weminder.data.Group
import com.weminder.data.Task
import com.weminder.utils.MOCK_GROUPS
import com.weminder.utils.MOCK_TASKS

class GroupViewModel : ViewModel() {

    var selected : MutableLiveData<Group> = MutableLiveData<Group>()
    val mockGroups: List<Group> = MOCK_GROUPS
    val mockTasks: List<Task> = MOCK_TASKS

    fun selectGroup(group: Group) {
        selected?.postValue(group)
    }
}