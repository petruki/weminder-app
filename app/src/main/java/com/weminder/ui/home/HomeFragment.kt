package com.weminder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.weminder.api.SocketHandler
import com.weminder.api.WEvent
import com.weminder.api.dto.GroupId
import com.weminder.data.Group
import com.weminder.databinding.FragmentHomeBinding
import com.weminder.ui.group.GroupListAdapter
import com.weminder.ui.group.GroupViewModel
import kotlinx.android.synthetic.main.bottom_bar_layout.*

class HomeFragment : Fragment(), GroupListAdapter.OnItemClickListener {

    private val groupViewModel: GroupViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var groupListAdapter: GroupListAdapter
    private lateinit var userGroups: List<Group>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        with(binding) {
            groupViewModel.groups.observe(viewLifecycleOwner, { groups ->
                userGroups = groups
                groupListAdapter = GroupListAdapter(userGroups, this@HomeFragment)
                recyclerGroup.adapter = groupListAdapter
                recyclerGroup.layoutManager = LinearLayoutManager(context)
            })

            includeBottomBar.fabAddGroup.setOnClickListener {
                val action = HomeFragmentDirections.actionNavHomeToGroupEditFragment(Group())
                findNavController().navigate(action)
            }
        }

        setupSocket()
        groupViewModel.getAllGroups()
        return binding.root
    }

    override fun onGroupClick(group: Group): Boolean {
        val action = HomeFragmentDirections.actionNavHomeToGroupDetailFragment(group)
        findNavController().navigate(action)
        return true
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext())
        SocketHandler.subscribe(WEvent.ON_FIND_USER_GROUPS) { onSyncGroups(it) }
        SocketHandler.emit(WEvent.FIND_USER_GROUPS, GroupId(""))
    }

    private fun onSyncGroups(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val groups = SocketHandler.getDTOGroupList(arg)
                groupViewModel.syncAllGroups(groups)
            }
    }

}