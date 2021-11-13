package com.weminder.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.weminder.api.SocketHandler
import com.weminder.api.WEvent
import com.weminder.api.dto.GroupAlias
import com.weminder.api.dto.GroupId
import com.weminder.data.Group
import com.weminder.databinding.FragmentSearchGroupBinding
import com.weminder.ui.group.GroupListAdapter
import com.weminder.ui.group.GroupViewModel
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_search_group.*

class SearchGroupFragment : Fragment(), GroupListAdapter.OnItemClickListener {

    private val searchViewModel: SearchGroupViewModel by viewModels()
    private val groupViewModel: GroupViewModel by viewModels()

    private lateinit var binding: FragmentSearchGroupBinding
    private lateinit var groupListAdapter: GroupListAdapter
    private lateinit var groupSelected: Group

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchGroupBinding.inflate(inflater, container, false)

        with(binding) {
            searchViewModel.groups.observe(viewLifecycleOwner) {
                groupListAdapter = GroupListAdapter(it, this@SearchGroupFragment)
                recyclerSearchGroup.adapter = groupListAdapter
                recyclerSearchGroup.layoutManager = LinearLayoutManager(context)
            }

            searchViewModel.selected.observe(viewLifecycleOwner, { group ->
                if (group != null) {
                    Toast.makeText(requireActivity(), "You already joined this group", Toast.LENGTH_SHORT).show()
                } else {
                    SocketHandler.emit(WEvent.JOIN_GROUP, GroupId(groupSelected.id))
                }
            })

            btnSearchGroup.setOnClickListener { searchGroup() }
        }

        return binding.root
    }

    override fun onGroupClick(group: Group): Boolean {
        if (AppUtils.isInternetAvailable(requireContext())) {
            setupSocket()
            groupSelected = group
            searchViewModel.selectGroupById(group.id)
            return true
        }
        return AppUtils.showUnavailable(requireContext())
    }

    private fun searchGroup(): Boolean {
        if (AppUtils.isInternetAvailable(requireContext())) {
            setupSocket()
            SocketHandler.emit(WEvent.FIND_GROUP, GroupAlias(txtSearchGroupAlias.text.toString()))
            return true
        }
        return AppUtils.showUnavailable(requireContext())
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext(), "")
        SocketHandler.subscribe(WEvent.ON_FIND_GROUP) { onFindGroup(it) }
        SocketHandler.subscribe(WEvent.ON_JOIN_GROUP) { onJoinGroup(it) }
    }

    // Socket Events

    private fun onFindGroup(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val group = SocketHandler.getDTO(Group::class.java, arg)
                searchViewModel.groups.postValue(listOf(group))
                SocketHandler.disconnect()
            }
    }

    private fun onJoinGroup(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val group = SocketHandler.getDTO(Group::class.java, arg)
                groupViewModel.insert(group)

                Toast.makeText(requireActivity(), "Joined ${group.name}", Toast.LENGTH_SHORT).show()
                SocketHandler.disconnect()

                findNavController().navigateUp()
            }
    }

}