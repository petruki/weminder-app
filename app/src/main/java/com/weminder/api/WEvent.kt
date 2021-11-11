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

    CREATE_TASK("create_task"),
    ON_CREATE_TASK("on_create_task"),
    UPDATE_TASK("update_task"),
    ON_UPDATE_TASK("on_update_task"),
    DELETE_TASK("delete_task"),
    ON_DELETE_TASK("on_delete_task"),

    FIND_GROUP("find_group"),
    ON_FIND_GROUP("on_find_group"),
    JOIN_GROUP("join_group"),
    ON_JOIN_GROUP("on_join_group"),

    ON_ERROR("on_error")
}