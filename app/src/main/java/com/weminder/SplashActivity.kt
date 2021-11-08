package com.weminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.activity_splash.*

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
    }

    private fun onEnter() {
        val userid = AppUtils.getUserId(this)

        finish()

        if (userid.isEmpty())
            startActivity(Intent(this, LoginActivity::class.java))
        else
            startActivity(Intent(this, DashboardActivity::class.java))
    }

}