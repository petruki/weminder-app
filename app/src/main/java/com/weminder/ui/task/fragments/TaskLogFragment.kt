package com.weminder.ui.task.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.weminder.databinding.FragmentTaskLogBinding

class TaskLogFragment : Fragment() {

    private val args: TaskLogFragmentArgs by navArgs()
    private lateinit var binding: FragmentTaskLogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskLogBinding.inflate(inflater, container, false)

        with(binding) {
            btnAddLog.setOnClickListener { onAddLog() }
            btnCancelLog.setOnClickListener { onCancel() }
        }

        return binding.root
    }

    private fun onCancel() {
        findNavController().navigateUp()
    }

    private fun onAddLog() {
        Toast.makeText(context, "Log Added", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

}