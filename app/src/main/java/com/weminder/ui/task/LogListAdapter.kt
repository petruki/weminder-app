package com.weminder.ui.task

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.weminder.R
import com.weminder.data.Log
import kotlinx.android.synthetic.main.item_log.view.*

class LogListAdapter(
    private val logs: List<Log>
) :
    RecyclerView.Adapter<LogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val log = logs[position]
        holder.bind(log)
    }

    override fun getItemCount(): Int = logs.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(log: Log) {
            with (itemView) {
                itemLogMessage.text = log.message
                itemLogCreatedAt.text = log.createdAt
            }
        }
    }
}