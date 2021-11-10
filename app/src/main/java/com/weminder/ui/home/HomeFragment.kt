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
import com.weminder.ui.group.GroupViewModel
import kotlinx.android.synthetic.main.bottom_bar_layout.*

class HomeFragment : Fragment(), GroupListAdapter.OnItemClickListener {

    private val groupViewModel: GroupViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var groupListAdapter: GroupListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        with(binding) {

            groupViewModel.getAllGroups()?.observe(viewLifecycleOwner, {
                groupListAdapter = GroupListAdapter(it, this@HomeFragment)
                recyclerGroup.adapter = groupListAdapter
                recyclerGroup.layoutManager = LinearLayoutManager(context)
            })

            includeBottomBar.fabAddGroup.setOnClickListener {
                val action = HomeFragmentDirections.actionNavHomeToGroupEditFragment(Group())
                findNavController().navigate(action)
            }
        }

        return binding.root
    }

    override fun onGroupClick(group: Group) {
        val action = HomeFragmentDirections.actionNavHomeToGroupDetailFragment(group.id)
        findNavController().navigate(action)
    }

}