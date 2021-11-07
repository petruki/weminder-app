package com.weminder.ui.login

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weminder.ui.login.fragments.LoginSignInFragment
import com.weminder.ui.login.fragments.LoginSignUpFragment

class LoginPageAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return LoginSignInFragment()
            1 -> return LoginSignUpFragment()
        }

        return LoginSignInFragment()
    }
}