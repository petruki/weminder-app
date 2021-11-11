package com.weminder.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.weminder.R

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

        fun updateUserId(context: Context, id: String) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.app_preferences_file_key), Context.MODE_PRIVATE)

            with(sharedPref.edit()) {
                putString(USER_ID, id)
                apply()
            }
        }

        fun getUserId(context: Context): String {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.app_preferences_file_key), Context.MODE_PRIVATE)

            return sharedPref.getString(USER_ID, "")!!
        }
    }

}
