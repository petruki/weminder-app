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
import com.weminder.api.SocketHandler
import com.weminder.api.WEvent
import com.weminder.api.dto.GroupId
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
                btnEditTask.setOnClickListener { editTask(task) }
                btnAddLogTask.setOnClickListener { addLogTask(task) }
                btnDeleteTask.setOnClickListener { deleteTask(task) }
            }
        })

        taskViewModel.selectTask(args.taskid)
        setupSocket()

        return binding.root
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext())
        SocketHandler.emit(WEvent.JOIN_ROOM, GroupId(args.groupId))

        SocketHandler.subscribe(WEvent.ON_UPDATE_TASK) { onUpdateTask(it) }
        SocketHandler.subscribe(WEvent.ON_DELETE_TASK) { onDeleteTask(it) }
    }

    private fun editTask(task: Task) {
        val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskEditFragment(task, task.groupId)
        findNavController().navigate(action)
    }

    private fun addLogTask(task: Task) {
        val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskLogFragment(task.id, task.groupId)
        findNavController().navigate(action)
    }

    private fun deleteTask(task: Task) {
        SocketHandler.emit(WEvent.DELETE_TASK, task)
        taskViewModel.delete(task)
        findNavController().navigateUp()
    }

    // Socket Events

    private fun onUpdateTask(arg: Array<Any>) {
        requireActivity().runOnUiThread {
            val task = SocketHandler.getDTO(Task::class.java, arg)
            taskViewModel.update(task)
        }
    }

    private fun onDeleteTask(arg: Array<Any>) {
        requireActivity().runOnUiThread {
            val task = SocketHandler.getDTO(Task::class.java, arg)
            taskViewModel.delete(task)
        }
    }
}