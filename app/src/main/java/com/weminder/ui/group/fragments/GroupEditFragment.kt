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
import com.weminder.data.Group
import com.weminder.databinding.FragmentGroupEditBinding
import com.weminder.ui.group.GroupViewModel

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
        if (group.id.isEmpty()) {
            Toast.makeText(context, "Group Created", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Group Edited", Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }

    private fun onCancel() {
        findNavController().navigateUp()
    }
}