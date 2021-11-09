package com.weminder.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weminder.api.APIResources
import com.weminder.api.WeminderAPI
import com.weminder.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val api: APIResources = WeminderAPI.api()
    val user : MutableLiveData<User> = MutableLiveData<User>()

    fun signUp(username: String, password: String) {
        val request = api.signup(User(username, password))

        request.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 201)
                    user.postValue(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                throw Exception("Unable to Sign Up")
            }
        })
    }

    fun singIn(username: String, password: String) {
        val request = api.login(User(username, password))

        request.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200)
                    user.postValue(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                throw Exception("Unable to Sign In")
            }

        })
    }

    fun me() {
        val request = api.me()

        request.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200)
                    user.postValue(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                throw Exception("Unable to load user")
            }
        })
    }
}