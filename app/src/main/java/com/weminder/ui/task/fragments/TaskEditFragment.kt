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
import com.weminder.api.dto.Error
import com.weminder.api.dto.GroupId
import com.weminder.data.Task
import com.weminder.databinding.FragmentTaskEditBinding
import com.weminder.ui.task.TaskViewModel
import com.weminder.utils.TASK_STATUS
import kotlinx.android.synthetic.main.fragment_task_edit.*

class TaskEditFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val args: TaskEditFragmentArgs by navArgs()

    private lateinit var binding: FragmentTaskEditBinding
    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        setupSocket()

        task.title = txtTaskTitle.text.toString()
        task.status = txtTaskStatus.selectedItem.toString()
        task.content = txtTaskContent.text.toString()

        if (task.id.isEmpty()) {
            SocketHandler.emit(WEvent.CREATE_TASK, task)
        } else {
            SocketHandler.emit(WEvent.UPDATE_TASK, task)
        }

        findNavController().navigateUp()
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext())
        SocketHandler.subscribe(WEvent.ON_CREATE_TASK) { onCreateTask(it) }
        SocketHandler.subscribe(WEvent.ON_UPDATE_TASK) { onUpdateTask(it) }
        SocketHandler.subscribe(WEvent.ON_ERROR) { onError(it) }
    }

    // Socket Events

    private fun onCreateTask(arg: Array<Any>) {
        SocketHandler.emit(WEvent.LEAVE_ROOM, GroupId(args.groupId))
        requireActivity().runOnUiThread {
            val task = SocketHandler.getDTO(Task::class.java, arg)
            taskViewModel.insert(task)

            Toast.makeText(context, "Task ${task.title} Created", Toast.LENGTH_SHORT).show()
            SocketHandler.getClient().disconnect()
            findNavController().navigateUp()
        }
    }

    private fun onUpdateTask(arg: Array<Any>) {
        SocketHandler.emit(WEvent.LEAVE_ROOM, GroupId(args.groupId))
        requireActivity().runOnUiThread {
            val task = SocketHandler.getDTO(Task::class.java, arg)
            taskViewModel.update(task)

            Toast.makeText(context, "Task ${task.title} Edited", Toast.LENGTH_SHORT).show()
            SocketHandler.getClient().disconnect()
            findNavController().navigateUp()
        }
    }

    private fun onError(arg: Array<Any>) {
        requireActivity().runOnUiThread {
            val error = SocketHandler.getDTO(Error::class.java, arg)
            Toast.makeText(context, error.error, Toast.LENGTH_SHORT).show()
            SocketHandler.getClient().disconnect()
        }
    }
}