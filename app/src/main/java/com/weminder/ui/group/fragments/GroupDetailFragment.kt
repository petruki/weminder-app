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
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.group_detail_content.view.*
import kotlinx.android.synthetic.main.group_detail_controls.view.*
import kotlinx.android.synthetic.main.group_detail_header.view.*

class GroupDetailFragment : Fragment(), TaskListAdapter.OnItemClickListener {

    private val groupViewModel: GroupViewModel by viewModels()
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

                // Setup Controls
                btnAddGroupTask.setOnClickListener { onAddGroupTask() }
                btnEditGroup.setOnClickListener { onEditGroup(group) }
                btnLeaveGroup.setOnClickListener { onLeaveGroup() }
            }
        })

        groupViewModel.selectGroup(args.groupid)
        setupSocket()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.emit(WEvent.LEAVE_ROOM, args.groupid)
        SocketHandler.disconnect()
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext())
        SocketHandler.emit(WEvent.JOIN_ROOM, GroupId(args.groupid))

        SocketHandler.subscribe(WEvent.ON_UPDATE_GROUP) { onUpdateGroup(it) }
        SocketHandler.subscribe(WEvent.ON_LEAVE_GROUP) { onLeaveGroup(it) }
        SocketHandler.subscribe(WEvent.ON_CREATE_TASK) { onCreateTask(it) }
    }

    private fun onAddGroupTask() {
        val newTask = Task(AppUtils.getUserId(requireContext()), args.groupid)
        val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToTaskEditFragment(newTask, args.groupid)
        findNavController().navigate(action)
    }

    private fun onEditGroup(group: Group) {
        val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToGroupEditFragment(group)
        findNavController().navigate(action)
    }

    private fun onLeaveGroup() {
        SocketHandler.emit(WEvent.LEAVE_GROUP, GroupId(args.groupid))
        groupViewModel.delete(selectedGroup)
        findNavController().navigateUp()
    }

    override fun onTaskClick(task: Task) {
        val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToTaskDetailFragment(task.id)
        findNavController().navigate(action)
    }

    // Socket Events

    private fun onUpdateGroup(arg: Array<Any>) {
        requireActivity().runOnUiThread {
            val group = SocketHandler.getDTO(Group::class.java, arg)
            groupViewModel.update(group)
        }
    }

    private fun onLeaveGroup(arg: Array<Any>) {
        requireActivity().runOnUiThread {
            val group = SocketHandler.getDTO(Group::class.java, arg)
            groupViewModel.update(group)
        }
    }

    private fun onCreateTask(arg: Array<Any>) {
        requireActivity().runOnUiThread {
            val task = SocketHandler.getDTO(Task::class.java, arg)
            groupViewModel.insertTask(task)

            tasks.add(task)
            taskListAdapter.notifyItemInserted(tasks.size)
        }
    }

}