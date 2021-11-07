package com.weminder.ui.group

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
import com.weminder.databinding.FragmentGroupDetailBinding
import com.weminder.ui.task.TaskListAdapter
import kotlinx.android.synthetic.main.group_detail_content.view.*
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
        loadGroup()

        groupViewModel.selected.observe(viewLifecycleOwner, {
            with( binding.root) {
                // Setup Info
                txtGroupInfoName.text = it.name

                // Setup Content
                val groupTasks = groupViewModel.mockTasks.filter { it.groupId == args.groupid }
                taskListAdapter = TaskListAdapter(groupTasks, this@GroupDetailFragment)
                recyclerGroupTasks.adapter = taskListAdapter
                recyclerGroupTasks.layoutManager = LinearLayoutManager(context)
            }
        })

        return binding.root
    }

    private fun loadGroup() {
        val group = groupViewModel.mockGroups.filter { it.id == args.groupid }
        groupViewModel.selectGroup(group[0])
    }

    override fun onTaskClick(task: Task) {
        val action = GroupDetailFragmentDirections.actionGroupDetailFragmentToTaskDetailFragment(task.id)
        findNavController().navigate(action)
    }
}