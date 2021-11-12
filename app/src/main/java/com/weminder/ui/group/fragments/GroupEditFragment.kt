package com.weminder.ui.group.fragments

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
import com.weminder.data.Group
import com.weminder.databinding.FragmentGroupEditBinding
import com.weminder.ui.group.GroupViewModel
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_group_edit.*

class GroupEditFragment : Fragment() {

    private val groupViewModel: GroupViewModel by viewModels()
    private val args: GroupEditFragmentArgs by navArgs()

    private lateinit var binding: FragmentGroupEditBinding
    private lateinit var group: Group

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        group = args.group
        binding = FragmentGroupEditBinding.inflate(inflater, container, false)

        with(binding) {
            txtGroupName.setText(group.name)
            txtGroupAlias.setText(group.alias)

            btnGroupCancel.setOnClickListener { onCancel() }
            btnGroupSave.setOnClickListener { onSave() }
        }

        return binding.root
    }

    private fun onSave() {
        if (AppUtils.isInternetAvailable(requireContext())) {
            setupSocket()

            group.name = txtGroupName.text.toString()
            group.alias = txtGroupAlias.text.toString()

            if (group.id.isEmpty()) {
                SocketHandler.emit(WEvent.CREATE_GROUP, group)
            } else {
                SocketHandler.emit(WEvent.JOIN_ROOM, GroupId(group.id))
                SocketHandler.emit(WEvent.UPDATE_GROUP, group)
            }
        } else
            AppUtils.showUnavailable(requireContext())
    }

    private fun onCancel() {
        findNavController().navigateUp()
    }

    private fun setupSocket() {
        SocketHandler.initSocket(requireContext())
        SocketHandler.subscribe(WEvent.ON_CREATE_GROUP) { onCreateGroup(it) }
        SocketHandler.subscribe(WEvent.ON_UPDATE_GROUP) { onUpdateGroup(it) }
        SocketHandler.subscribe(WEvent.ON_ERROR) { onError(it) }
    }

    // Socket Events

    private fun onCreateGroup(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val group = SocketHandler.getDTO(Group::class.java, arg)
                groupViewModel.insert(group)

                Toast.makeText(context, "Group ${group.name} Created", Toast.LENGTH_SHORT).show()
                SocketHandler.getClient().disconnect()
                findNavController().navigateUp()
            }
    }

    private fun onUpdateGroup(arg: Array<Any>) {
        SocketHandler.emit(WEvent.LEAVE_ROOM, GroupId(group.id))
        if (isAdded)
            requireActivity().runOnUiThread {
                val group = SocketHandler.getDTO(Group::class.java, arg)
                groupViewModel.update(group)

                Toast.makeText(context, "Group ${group.name} Edited", Toast.LENGTH_SHORT).show()
                SocketHandler.getClient().disconnect()
                findNavController().navigateUp()
            }
    }

    private fun onError(arg: Array<Any>) {
        if (isAdded)
            requireActivity().runOnUiThread {
                val error = SocketHandler.getDTO(Error::class.java, arg)
                Toast.makeText(context, error.error, Toast.LENGTH_SHORT).show()
                SocketHandler.getClient().disconnect()
            }
    }
}