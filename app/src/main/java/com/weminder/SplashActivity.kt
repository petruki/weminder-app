package com.weminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.weminder.utils.AppUtils
import com.weminder.utils.USER_ID
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Starter Activity
 *
 * It verifies if user is already registered.
 * If device is connected to the Internet, it will delay by 2 seconds,
 * Otherwise, the app will automatically redirect to the Dashboard Activity
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        buttonGoDashboard.setOnClickListener { onEnter() }
        initApp()
    }

    private fun initApp() {
        if (AppUtils.isInternetAvailable(this))
            Handler(Looper.getMainLooper()).postDelayed({ onEnter() }, 2000)
        else
            onEnter()
    }

    private fun onEnter() {
        val userid = AppUtils.getUser(this, USER_ID)

        finish()

        if (userid.isEmpty())
            startActivity(Intent(this, LoginActivity::class.java))
        else
            startActivity(Intent(this, DashboardActivity::class.java))
    }

}