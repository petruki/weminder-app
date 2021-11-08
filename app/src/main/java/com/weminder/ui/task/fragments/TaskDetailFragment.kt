package com.weminder.ui.task.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.weminder.data.Task
import com.weminder.databinding.FragmentTaskDetailBinding
import com.weminder.ui.task.LogListAdapter
import com.weminder.ui.task.TaskViewModel
import kotlinx.android.synthetic.main.task_detail_content.view.*
import kotlinx.android.synthetic.main.task_detail_controls.view.*
import kotlinx.android.synthetic.main.task_detail_header.view.*

class TaskDetailFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val args: TaskDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentTaskDetailBinding
    private lateinit var logListAdapter: LogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        loadTask()

        taskViewModel.selected.observe(viewLifecycleOwner, { task ->
            with( binding.root) {
                // Setup Info
                txtTaskInfoTitle.text = task.title
                txtTaskInfoStatus.text = task.status
                txtTaskInfoContent.text = task.content

                // Setup Content
                logListAdapter = LogListAdapter(task.log)
                recyclerTaskLogs.adapter = logListAdapter
                recyclerTaskLogs.layoutManager = LinearLayoutManager(context)

                // Setup Controls
                btnEditTask.setOnClickListener { onEditTask(task) }
                btnAddLogTask.setOnClickListener { onAddLogTask(task) }
                btnDeleteTask.setOnClickListener { onDeleteTask(task) }
            }
        })

        return binding.root
    }

    private fun loadTask() {
        val task = taskViewModel.mockTasks.filter { it.id == args.taskid }
        taskViewModel.selectTask(task[0])
    }

    private fun onEditTask(task: Task) {
        val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskEditFragment(task)
        findNavController().navigate(action)
    }

    private fun onAddLogTask(task: Task) {
        val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskLogFragment(task.id, task.groupId)
        findNavController().navigate(action)
    }

    private fun onDeleteTask(task: Task) {
        findNavController().navigateUp()
    }
}