package com.weminder.ui.group

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.weminder.R
import com.weminder.data.Group
import kotlinx.android.synthetic.main.item_group.view.*

class GroupListAdapter(
    private val groups: List<Group>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        holder.bind(group, listener)
    }

    override fun getItemCount(): Int = groups.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(group: Group, listener: OnItemClickListener) {
            with (itemView) {
                itemView.itemGroupName.text = group.name
                setOnClickListener { listener.onGroupClick(group) }
            }
        }
    }

    interface OnItemClickListener {
        fun onGroupClick(group: Group) { }
    }

}