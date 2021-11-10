package com.weminder.api

enum class WEvent(val arg: String) {
    ME("me"),
    ON_ME("on_me"),

    CREATE_GROUP("create_group"),
    ON_CREATE_GROUP("on_create_group"),

    ON_ERROR("on_error")
}