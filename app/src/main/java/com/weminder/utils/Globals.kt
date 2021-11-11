package com.weminder.utils

import com.weminder.R

const val USER_ID = "USER_ID"

val TASK_STATUS: List<List<Any>> = listOf(
    listOf("Opened", R.drawable.icon_task_open),
    listOf("In Progress", R.drawable.icon_task_progress),
    listOf("Blocked", R.drawable.icon_task_block),
    listOf("Done", R.drawable.icon_task_done)
)