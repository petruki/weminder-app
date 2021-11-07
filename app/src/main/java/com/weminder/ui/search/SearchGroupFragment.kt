package com.weminder.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.weminder.databinding.FragmentSearchGroupBinding

class SearchGroupFragment : Fragment() {

    private var _binding: FragmentSearchGroupBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchGroupBinding.inflate(inflater, container, false)

        return binding.root
    }

}