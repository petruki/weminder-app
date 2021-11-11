package com.weminder.ui.group.fragments

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
import com.weminder.data.Group
import com.weminder.data.Task
import com.weminder.databinding.FragmentGroupDetailBinding
import com.weminder.ui.group.GroupViewModel
import com.weminder.ui.task.TaskListAdapter
import com.weminder.ui.task.TaskViewModel
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.group_detail_content.view.*
import kotlinx.android.synthetic.main.group_detail_controls.view.*
import kotlinx.android.synthetic.main.group_detail_header.view.*

class GroupDetailFragment : Fragment(), TaskListAdapter.OnItemClickListener {

    private val groupViewModel: GroupViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val args: GroupDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentGroupDetailBinding
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var selectedGroup: Group
    private lateinit var tasks: MutableList<Task>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupDetailBinding.inflate(inflater, container, false)

        selectedGroup = args.group
        groupViewModel.selected.observe(viewLifecycleOwner, { group ->
            selectedGroup = group
            with( binding.root) {
                // Setup Info
                txtGroupInfoName.text = group.name
                txtGroupInfoAlias.text = group.alias

                // Setup Content
                groupViewModel.groupTasks.observe(viewLifecycleOwner, {
                    tasks = it.toMutableList()
                    taskListAdapter = TaskListAdapter(tasks, this@GroupDetailFragment)
                    recyclerGroupTasks.adapter = taskListAdapter
                    recyclerGroupTasks.layoutManager = LinearLayoutManager(context)
                })

                // Sync Tasks
                SocketHandler.emit(WEvent.LIST_TASKS, GroupId(selectedGroup.id))

                // Setup Controls
                btnAddGroupTask.setOnClickListener { onAddGroupTask() }
                btnEditGroup.setOnClickListener { onEditGroup(group) }
                btnLeaveGroup.setOnClickListener { onLeaveGroup() }
            }
        })

        groupViewModel.selectGroup(selectedGroup.id)
        setupSocket()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.emit(WEvent.LEAVE_ROOM, GroupId(selectedGroup.id))
        SocketHandler.disconnect()
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext())
        SocketHandler.emit(WEvent.JOIN_ROOM, GroupId(selectedGroup.id))

        // Sync Events
        SocketHandler.subscribe(WEvent.ON_FIND_GROUP) { onSyncGroup(it) }
        SocketHandler.subscribe(WEvent.ON_LIST_TASKS) { onSyncGroupTasks(it) }

        // Listener Events
        SocketHandler.subscribe(WEvent.ON_UPDATE_GROUP) { onUpdateGroup(it) }
        SocketHandler.subscribe(WEvent.ON_LEAVE_GROUP) { onLeaveGroup(it) }
        SocketHandler.subscribe(WEvent.ON_CREATE_TASK) { onCreateTask(it) }
        SocketHandler.subscribe(WEvent.ON_UPDATE_TASK) { onUpdateTask(it) }
        SocketHandler.subscribe(WEvent.ON_DELETE_TASK) { onDeleteTask(it) }

        // Trigger sync group
        SocketHandler.emit(WEvent.FIND_GROUP, GroupId(selectedGroup.id))
    }

    private fun onAddGroupTask() {
        if (AppUtils.isInternetAvailable(requireContext())) {
            val newTask = Task(AppUtils.getUserId(requireContext()), selectedGroup.id)
            val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToTaskEditFragment(newTask, selectedGroup.id)
            return findNavController().navigate(action)
        }
        AppUtils.showUnavailable(requireContext())
    }

    private fun onEditGroup(group: Group) {
        if (AppUtils.isInternetAvailable(requireContext())) {
            val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToGroupEditFragment(group)
            return findNavController().navigate(action)
        }
        AppUtils.showUnavailable(requireContext())
    }

    private fun onLeaveGroup(): Boolean {
        if (AppUtils.isInternetAvailable(requireContext())) {
            SocketHandler.emit(WEvent.LEAVE_GROUP, GroupId(args.group.id))
            groupViewModel.delete(selectedGroup)
            findNavController().navigateUp()
            return true
        }
        return AppUtils.showUnavailable(requireContext())
    }

    override fun onTaskClick(task: Task) {
        val action =
            GroupDetailFragmentDirections.actionGroupDetailFragmentToTaskDetailFragment(
                selectedGroup.id,
                task
            )
        findNavController().navigate(action)
    }

    // Socket Events

    private fun onUpdateGroup(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val group = SocketHandler.getDTO(Group::class.java, arg)
                groupViewModel.update(group)
            }
    }

    private fun onLeaveGroup(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val group = SocketHandler.getDTO(Group::class.java, arg)
                groupViewModel.update(group)
            }
    }

    private fun onCreateTask(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val task = SocketHandler.getDTO(Task::class.java, arg)
                groupViewModel.insertTask(task)

                tasks.add(task)
                taskListAdapter.notifyItemInserted(tasks.size)
            }
    }

    private fun onUpdateTask(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val task = SocketHandler.getDTO(Task::class.java, arg)
                taskViewModel.update(task)

                val index = tasks.indexOf(task)
                tasks[index] = task
                taskListAdapter.notifyItemChanged(index)
            }
    }

    private fun onDeleteTask(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val task = SocketHandler.getDTO(Task::class.java, arg)
                taskViewModel.delete(task)

                val index = tasks.indexOf(task)
                tasks.removeAt(index)
                taskListAdapter.notifyItemRemoved(index)
            }
    }

    private fun onSyncGroup(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val group = SocketHandler.getDTO(Group::class.java, arg)
                groupViewModel.update(group)
            }
    }

    private fun onSyncGroupTasks(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val tasks = SocketHandler.getDTOTaskList(arg)
                groupViewModel.syncAllGroupTasks(tasks, selectedGroup.id)
            }
    }

}