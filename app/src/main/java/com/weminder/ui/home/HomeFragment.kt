package com.weminder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.weminder.data.Group
import com.weminder.databinding.FragmentHomeBinding
import com.weminder.ui.group.GroupListAdapter

class HomeFragment : Fragment(), GroupListAdapter.OnItemClickListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var groupListAdapter: GroupListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        groupListAdapter = GroupListAdapter(homeViewModel.mockGroups, this)
        binding.recyclerGroup.adapter = groupListAdapter
        binding.recyclerGroup.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onGroupClick(group: Group) {
        val action = HomeFragmentDirections.actionNavHomeToGroupDetailFragment(group.id)
        findNavController().navigate(action)
    }

}