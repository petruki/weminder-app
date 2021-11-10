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
import com.weminder.data.Group
import com.weminder.data.Task
import com.weminder.databinding.FragmentGroupDetailBinding
import com.weminder.ui.group.GroupViewModel
import com.weminder.ui.task.TaskListAdapter
import kotlinx.android.synthetic.main.group_detail_content.view.*
import kotlinx.android.synthetic.main.group_detail_controls.view.*
import kotlinx.android.synthetic.main.group_detail_header.view.*

class GroupDetailFragment : Fragment(), TaskListAdapter.OnItemClickListener {

    private val groupViewModel: GroupViewModel by viewModels()
    private val args: GroupDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentGroupDetailBinding
    private lateinit var taskListAdapter: TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupDetailBinding.inflate(inflater, container, false)

        groupViewModel.selected.observe(viewLifecycleOwner, { group ->
            with( binding.root) {
                // Setup Info
                txtGroupInfoName.text = group.name
                txtGroupInfoAlias.text = group.alias

                // Setup Content
                groupViewModel.groupTasks.observe(viewLifecycleOwner, {
                    taskListAdapter = TaskListAdapter(it, this@GroupDetailFragment)
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
        SocketHandler.disconnect()
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext())
    }

    private fun onAddGroupTask() {
        val newTask = Task("1", args.groupid)
        val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToTaskEditFragment(newTask)
        findNavController().navigate(action)
    }

    private fun onEditGroup(group: Group) {
        val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToGroupEditFragment(group)
        findNavController().navigate(action)
    }

    private fun onLeaveGroup() {
        findNavController().navigateUp()
    }

    override fun onTaskClick(task: Task) {
        val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToTaskDetailFragment(task.id)
        findNavController().navigate(action)
    }

}