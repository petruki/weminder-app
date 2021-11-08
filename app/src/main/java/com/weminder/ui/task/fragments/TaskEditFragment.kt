package com.weminder.ui.task.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.weminder.data.Task
import com.weminder.databinding.FragmentTaskEditBinding
import com.weminder.ui.task.TaskViewModel
import com.weminder.utils.TASK_STATUS

class TaskEditFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val args: TaskEditFragmentArgs by navArgs()

    private lateinit var binding: FragmentTaskEditBinding
    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        task = args.task
        binding = FragmentTaskEditBinding.inflate(inflater, container, false)

        with (binding) {
            txtTaskContent.setText(task.content)
            txtTaskTitle.setText(task.title)

            val map = TASK_STATUS.withIndex().associate { it.value[0] to it.index }
            map[task.status]?.let { txtTaskStatus.setSelection(it) }

            btnTaskSave.setOnClickListener { onSave() }
            btnTaskCancel.setOnClickListener { onCancel() }
        }

        return binding.root
    }

    private fun onCancel() {
        findNavController().navigateUp()
    }

    private fun onSave() {
        if (task.id.isEmpty()) {
            Toast.makeText(context, "Task Created", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Edited", Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }
}