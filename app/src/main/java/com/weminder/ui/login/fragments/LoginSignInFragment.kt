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
import com.weminder.databinding.FragmentLoginSignInBinding
import com.weminder.ui.login.LoginViewModel
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_login_sign_in.*

/**
 * Fragment: fragment_login_sing_in.xml
 */
class LoginSignInFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginSignInBinding

    companion object {
        const val TITLE = "Login"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginSignInBinding.inflate(inflater, container, false)
        binding.buttonLogin.setOnClickListener { onClickLogin() }

        loginViewModel.user.observe(viewLifecycleOwner, { onSignIn(it) })

        return binding.root
    }

    private fun onClickLogin() {
        val username = textLogin.text.toString()
        val password = textPassword.text.toString()

        loginViewModel.singIn(username, password)
    }

    private fun onSignIn(user: User) {
        activity?.let {
            AppUtils.updateUser(it, user)
            it.finish()
        }

        startActivity(Intent(activity, DashboardActivity::class.java))
    }

}