package com.weminder.api

enum class WEvent(val arg: String) {
    ME("me"),
    ON_ME("on_me"),
    ON_ERROR("on_error")
}