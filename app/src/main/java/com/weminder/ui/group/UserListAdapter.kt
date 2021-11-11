package com.weminder.ui.group

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.weminder.R
import com.weminder.data.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserListAdapter(private val users: List<User>) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with (itemView) {
                itemUserName.text = user.username
            }
        }
    }

}