package com.weminder.ui.login.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.weminder.DashboardActivity
import com.weminder.data.User
import com.weminder.databinding.FragmentLoginSignUpBinding
import com.weminder.ui.login.LoginViewModel
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_login_sign_up.*

/**
 * Fragment: fragment_login_sing_up.xml
 */
class LoginSignUpFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginSignUpBinding

    companion object {
        const val TITLE = "Sign Up"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginSignUpBinding.inflate(inflater, container, false)
        binding.btnSignUp.setOnClickListener { onClickSignUp() }

        loginViewModel.user.observe(viewLifecycleOwner, { onSignUp(it) })

        return binding.root
    }

    private fun onClickSignUp() {
        val username = txtSignUpUsername.text.toString()
        val password = txtSignUpPassword.text.toString()

        loginViewModel.signUp(username, password)
    }

    private fun onSignUp(user: User) {
        activity?.let {
            AppUtils.updateUser(it, user)
            it.finish()
        }

        startActivity(Intent(activity, DashboardActivity::class.java))
    }

}