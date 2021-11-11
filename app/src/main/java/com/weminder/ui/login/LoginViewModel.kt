package com.weminder.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weminder.api.APIResources
import com.weminder.api.WeminderAPI
import com.weminder.data.User
import com.weminder.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    private val api: APIResources = WeminderAPI.api()
    val user: MutableLiveData<User> = MutableLiveData<User>()

    fun signUp(username: String, password: String) {
        val request = api.signup(User(username, password))

        request.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 201)
                    user.postValue(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.message?.let { Log.e("SIGN_UP", it) }
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
                t.message?.let { Log.e("SIGN_IN", it) }
            }

        })
    }

    fun wipeDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.taskDao()?.deleteAll()
                database?.groupDao()?.deleteAll()
                database?.userDao()?.deleteAll()
            }
        }
    }
}