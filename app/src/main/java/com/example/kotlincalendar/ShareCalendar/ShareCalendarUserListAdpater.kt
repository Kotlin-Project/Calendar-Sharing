package com.example.kotlincalendar.ShareCalendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlincalendar.Entity.User
import com.example.kotlincalendar.R
import com.example.kotlincalendar.databinding.ShareCalendarMemberItemBinding

class ShareCalendarUserListAdpater(private val userList: List<User>) : RecyclerView.Adapter<ShareCalendarUserListAdpater.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ShareCalendarMemberItemBinding.bind(itemView)

        fun bind(user: User) {
            binding.memberUserName.text = user.Name
            Glide.with(itemView.context)
                .load(user.Profile_img)
                .into(binding.memberImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.share_calendar_member_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("ShareCalendarScheduleAdd123123123333", "123123123selectedDate: $userList")
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}