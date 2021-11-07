package com.weminder.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weminder.data.Task
import com.weminder.utils.MOCK_TASKS

class TaskViewModel : ViewModel() {

    var selected : MutableLiveData<Task> = MutableLiveData<Task>()
    val mockTasks: List<Task> = MOCK_TASKS

    fun selectTask(task: Task) {
        selected?.postValue(task)
    }
}