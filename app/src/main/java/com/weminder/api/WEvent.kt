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

    ON_ERROR("on_error")
}