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
import com.weminder.api.SocketHandler
import com.weminder.api.WEvent
import com.weminder.api.dto.GroupId
import com.weminder.api.dto.LogMessage
import com.weminder.data.Task
import com.weminder.databinding.FragmentTaskLogBinding
import com.weminder.ui.task.TaskViewModel
import kotlinx.android.synthetic.main.fragment_task_log.*

class TaskLogFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val args: TaskLogFragmentArgs by navArgs()
    private lateinit var binding: FragmentTaskLogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskLogBinding.inflate(inflater, container, false)

        with(binding) {
            btnAddLog.setOnClickListener { addLog() }
            btnCancelLog.setOnClickListener { cancel() }
        }

        setupSocket()
        return binding.root
    }

    private fun cancel() {
        findNavController().navigateUp()
    }

    private fun addLog() {
        SocketHandler.emit(WEvent.ADD_LOG,
            LogMessage(args.groupId, args.taskId, txtTaskLogMessage.text.toString()))
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext())
        SocketHandler.emit(WEvent.JOIN_ROOM, GroupId(args.groupId))

        SocketHandler.subscribe(WEvent.ON_UPDATE_TASK) { onUpdateTask(it) }
        SocketHandler.subscribe(WEvent.ON_ERROR) { onError() }
    }

    // Socket Events

    private fun onUpdateTask(arg: Array<Any>) {
        SocketHandler.emit(WEvent.LEAVE_ROOM, GroupId(args.groupId))
        if (isAdded)
            requireActivity().runOnUiThread {
                val task = SocketHandler.getDTO(Task::class.java, arg)
                taskViewModel.update(task)

                Toast.makeText(context, "Log added", Toast.LENGTH_SHORT).show()
                SocketHandler.getClient().disconnect()
                findNavController().navigateUp()
            }
    }

    private fun onError() {
        if (isAdded)
            requireActivity().runOnUiThread {
                Toast.makeText(context, "Failed to add log", Toast.LENGTH_SHORT).show()
            }
    }

}