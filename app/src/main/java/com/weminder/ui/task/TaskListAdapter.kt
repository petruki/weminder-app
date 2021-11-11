package com.weminder.ui.task

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.weminder.R
import com.weminder.data.Task
import com.weminder.utils.TASK_STATUS
import kotlinx.android.synthetic.main.item_task.view.*

class TaskListAdapter(
    private val tasks: List<Task>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, listener)
    }

    override fun getItemCount(): Int = tasks.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task, listener: OnItemClickListener) {
            with (itemView) {
                itemTaskTitle.text = task.title

                val status = TASK_STATUS.filter { list -> list[0] == task.status }[0]
                taskIcon.setImageResource(status[1] as Int)
                setOnClickListener { listener.onTaskClick(task) }
            }
        }
    }

    interface OnItemClickListener {
        fun onTaskClick(task: Task) { }
    }

}