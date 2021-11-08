package com.weminder.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val userid: MutableLiveData<String> = MutableLiveData<String>()

    fun signUp(username: String, password: String) {
        userid.postValue(username)
    }

    fun singIn(username: String, password: String) {
        userid.postValue(username)
    }
}