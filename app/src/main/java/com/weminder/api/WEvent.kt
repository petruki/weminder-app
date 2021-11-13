package com.weminder.api

enum class WEvent(val arg: String) {
    ME("me"),
    ON_ME("on_me"),

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
    GET_TASK("get_task"),
    ON_GET_TASK("on_get_task"),
    ADD_LOG("add_log"),

    FIND_GROUP_USERS("find_group_users"),
    ON_FIND_GROUP_USERS("on_find_group_users"),

    FIND_GROUP("find_group"),
    ON_FIND_GROUP("on_find_group"),
    JOIN_GROUP("join_group"),
    ON_JOIN_GROUP("on_join_group"),
    FIND_USER_GROUPS("find_user_groups"),
    ON_FIND_USER_GROUPS("on_find_user_groups"),
    LIST_TASKS("list_tasks"),
    ON_LIST_TASKS("on_list_tasks"),

    ON_ERROR("on_error")
}