package com.weminder.api

enum class WEvent(val arg: String) {
    ME("me"),
    ON_ME("on_me"),

    JOIN_ROOM("join_room"),
    LEAVE_ROOM("leave_room"),

    CREATE_GROUP("create_group"),
    ON_CREATE_GROUP("on_create_group"),
    UPDATE_GROUP("update_group"),
    ON_UPDATE_GROUP("on_update_group"),
    LEAVE_GROUP("leave_group"),
    ON_LEAVE_GROUP("on_leave_group"),

    ON_CREATE_TASK("on_create_task"),

    ON_ERROR("on_error")
}