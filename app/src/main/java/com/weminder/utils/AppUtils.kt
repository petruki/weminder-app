package com.weminder.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.weminder.R
import com.weminder.data.User

class AppUtils {

    companion object {

        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return result
        }

        fun showUnavailable(context: Context): Boolean {
            Toast.makeText(context, "Cannot perform this operation offline", Toast.LENGTH_SHORT).show()
            return false
        }

        fun updateUser(context: Context, user: User) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.app_preferences_file_key), Context.MODE_PRIVATE)

            with(sharedPref.edit()) {
                putString(USER_ID, user.id)
                putString(USER_NAME, user.username)
                apply()
            }
        }

        fun getUser(context: Context, key: String): String {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.app_preferences_file_key), Context.MODE_PRIVATE)

            return sharedPref.getString(key, "")!!
        }
    }

}
