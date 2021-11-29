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
import androidx.recyclerview.widget.LinearLayoutManager
import com.weminder.api.SocketHandler
import com.weminder.api.WEvent
import com.weminder.api.dto.Error
import com.weminder.api.dto.TaskId
import com.weminder.data.Task
import com.weminder.databinding.FragmentTaskDetailBinding
import com.weminder.ui.task.LogListAdapter
import com.weminder.ui.task.TaskViewModel
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.task_detail_content.view.*
import kotlinx.android.synthetic.main.task_detail_controls.view.*
import kotlinx.android.synthetic.main.task_detail_header.view.*

/**
 * Fragment: fragment_task_detail.xml
 */
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
                logListAdapter = LogListAdapter(task.log.reversed())
                recyclerTaskLogs.adapter = logListAdapter
                recyclerTaskLogs.layoutManager = LinearLayoutManager(context)

                // Setup Controls
                btnEditTask.setOnClickListener { editTask(task) }
                btnAddLogTask.setOnClickListener { addLogTask(task) }
                btnDeleteTask.setOnClickListener { deleteTask(task) }
            }
        })

        taskViewModel.selectTask(args.task.id)
        setupSocket()

        return binding.root
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext(), args.groupId)

        // Sync Events
        SocketHandler.subscribe(WEvent.ON_GET_TASK) { onSyncTask(it) }

        // Listener Events
        SocketHandler.subscribe(WEvent.ON_UPDATE_TASK) { onUpdateTask(it) }
        SocketHandler.subscribe(WEvent.ON_DELETE_TASK) { onDeleteTask(it) }
        SocketHandler.subscribe(WEvent.ON_ERROR) { onError(it) }

        // Triggers sync task
        SocketHandler.emit(WEvent.GET_TASK, TaskId(args.task.id))
    }

    private fun editTask(task: Task) {
        if (AppUtils.isInternetAvailable(requireContext())) {
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskEditFragment(task, task.groupId)
            return findNavController().navigate(action)
        }

        AppUtils.showUnavailable(requireContext())
    }

    private fun addLogTask(task: Task) {
        if (AppUtils.isInternetAvailable(requireContext())) {
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskLogFragment(
                task.id,
                task.groupId
            )
            return findNavController().navigate(action)
        }

        AppUtils.showUnavailable(requireContext())
    }

    private fun deleteTask(task: Task): Boolean {
        if (AppUtils.isInternetAvailable(requireContext())) {
            SocketHandler.emit(WEvent.DELETE_TASK, task)
            taskViewModel.delete(task)
            findNavController().navigateUp()
            return true
        }

        return AppUtils.showUnavailable(requireContext())
    }

    // Socket Events

    private fun onUpdateTask(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val task = SocketHandler.getDTO(Task::class.java, arg)
                taskViewModel.update(task)
            }
    }

    private fun onDeleteTask(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val task = SocketHandler.getDTO(Task::class.java, arg)
                taskViewModel.delete(task)

                Toast.makeText(context, "Task ${task.title} has been deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
    }

    private fun onSyncTask(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val task = SocketHandler.getDTO(Task::class.java, arg)
                taskViewModel.update(task)
            }
    }

    private fun onError(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val error = SocketHandler.getDTO(Error::class.java, arg)
                if (error.status == 404) {
                    taskViewModel.delete(args.task)

                    Toast.makeText(context, "Task ${args.task.title} has been deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
    }
}