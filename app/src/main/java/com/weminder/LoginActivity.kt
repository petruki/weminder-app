package com.weminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.weminder.ui.login.LoginPageAdapter
import com.weminder.ui.login.fragments.LoginSignInFragment
import com.weminder.ui.login.fragments.LoginSignUpFragment

/**
 * Sign Up/In activity
 * It displays when the app does not have any user registered,
 * Or user has logged out from their account.
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewLogin: ViewPager2 = findViewById(R.id.viewPagerLogin)

        viewLogin.adapter = LoginPageAdapter(this)
        TabLayoutMediator(
            tabLayout, viewLogin
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0)
                tab.text = LoginSignInFragment.TITLE
            else
                tab.text = LoginSignUpFragment.TITLE
        }.attach()
    }

}