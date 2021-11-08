package com.weminder.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.weminder.data.Group
import com.weminder.databinding.FragmentSearchGroupBinding
import com.weminder.ui.group.GroupListAdapter
import kotlinx.android.synthetic.main.fragment_search_group.*

class SearchGroupFragment : Fragment(), GroupListAdapter.OnItemClickListener {

    private val searchViewModel: SearchGroupViewModel by viewModels()

    private lateinit var binding: FragmentSearchGroupBinding
    private lateinit var groupListAdapter: GroupListAdapter

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

            btnSearchGroup.setOnClickListener { onSearchGroup() }
        }

        return binding.root
    }

    override fun onGroupClick(group: Group) {
        val action = SearchGroupFragmentDirections.actionNavSearchToGroupDetailFragment(group.id)
        findNavController().navigate(action)
    }

    private fun onSearchGroup() {
        val groupAlias = txtSearchGroupAlias.text.toString()
        searchViewModel.findGroupsByAlias(groupAlias)
    }

}