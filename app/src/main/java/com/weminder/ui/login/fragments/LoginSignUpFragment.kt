package com.weminder.ui.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.weminder.databinding.FragmentLoginSignUpBinding

class LoginSignUpFragment : Fragment() {

    private lateinit var binding: FragmentLoginSignUpBinding

    companion object {
        const val TITLE = "Sign Up"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

}