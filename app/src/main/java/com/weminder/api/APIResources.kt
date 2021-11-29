package com.weminder.api

import com.weminder.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Authentication resources
 */
interface APIResources {

    @POST("/login")
    fun login(@Body user: User): Call<User>

    @POST("/signup")
    fun signup(@Body user: User): Call<User>

}