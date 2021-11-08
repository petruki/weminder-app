package com.weminder.ui.login.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.weminder.DashboardActivity
import com.weminder.databinding.FragmentLoginSignInBinding
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_login_sign_in.*

class LoginSignInFragment : Fragment() {

    private lateinit var binding: FragmentLoginSignInBinding

    companion object {
        const val TITLE = "Login"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginSignInBinding.inflate(inflater, container, false)
        binding.buttonLogin.setOnClickListener { onLogin() }
        return binding.root
    }

    private fun onLogin() {
        val username = textLogin.text.toString()
        val password = textPassword.text.toString()

        activity?.let {
            AppUtils.updateUserId(it, username)
            it.finish()
        }

        startActivity(Intent(activity, DashboardActivity::class.java))
    }

}