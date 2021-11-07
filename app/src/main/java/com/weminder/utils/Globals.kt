package com.weminder.utils

import com.weminder.R
import com.weminder.data.Group
import com.weminder.data.Log
import com.weminder.data.Task
import java.util.*

val TASK_STATUS: List<List<Any>> = listOf(
    listOf("Opened", R.drawable.icon_task_open),
    listOf("In Progress", R.drawable.icon_task_progress),
    listOf("Blocked", R.drawable.icon_task_block),
    listOf("Done", R.drawable.icon_task_done)
)

val MOCK_GROUPS: List<Group> = listOf(
    Group("1", "Group 1", "group_1", emptyList()),
    Group("2", "Group 2", "group_2", emptyList()),
    Group("3", "Group 3", "group_3", emptyList()),
)

val MOCK_LOGS_1: List<Log> = listOf(
    Log("Message 1", Date().toString()),
    Log("Message 2", Date().toString()),
    Log("Message 3", Date().toString())
)

val MOCK_LOGS_2: List<Log> = listOf(
    Log("Message 4", Date().toString()),
    Log("Message 5", Date().toString()),
    Log("Message 6", Date().toString())
)

val MOCK_TASKS: List<Task> = listOf(
    Task("1", "Task 1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book", "Opened", MOCK_LOGS_1, "user1", "1"),
    Task("2", "Task 2", "This is task 2", "In Progress", MOCK_LOGS_2, "user1", "1"),
    Task("3", "Task 3", "This is task 3", "Done", emptyList(), "user1", "2")
)