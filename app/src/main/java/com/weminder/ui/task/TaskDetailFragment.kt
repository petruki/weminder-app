package com.weminder.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.weminder.databinding.FragmentTaskDetailBinding
import kotlinx.android.synthetic.main.task_detail_content.view.*
import kotlinx.android.synthetic.main.task_detail_header.view.*

class TaskDetailFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val args: TaskDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentTaskDetailBinding
    private lateinit var logListAdapter: LogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        loadTask()

        taskViewModel.selected.observe(viewLifecycleOwner, {
            with( binding.root) {
                // Setup Info
                txtTaskInfoTitle.text = it.title
                txtTaskInfoStatus.text = it.status
                txtTaskInfoContent.text = it.content

                // Setup Content
                logListAdapter = LogListAdapter(it.log)
                recyclerTaskLogs.adapter = logListAdapter
                recyclerTaskLogs.layoutManager = LinearLayoutManager(context)
            }
        })

        return binding.root
    }

    private fun loadTask() {
        val task = taskViewModel.mockTasks.filter { it.id == args.taskid }
        taskViewModel.selectTask(task[0])
    }
}