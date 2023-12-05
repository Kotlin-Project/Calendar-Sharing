package com.example.kotlincalendar.ShareCalendar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.Entity.ShareCalendar
import com.example.kotlincalendar.R

class ShareCalendarListAdapter(
    val context: Context,
    val calendarList:List<ShareCalendar>,
    val userEmail:String) : RecyclerView.Adapter<ShareCalendarListAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sharecalendarlistitem, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val currentItem = calendarList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return calendarList.size
    }

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val calendarTitle: TextView = itemView.findViewById(R.id.calendarTitle)
        private val calendarCategory: TextView = itemView.findViewById(R.id.calendarCategory)
        private val calendarImage:ImageView=itemView.findViewById(R.id.calendarImage)

        // 아이템 클릭 리스너 설정
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = calendarList[position]
                    val intent = Intent(context, ShareCalendarMain::class.java)
                    intent.putExtra("shareCalendarId", clickedItem.shareCalendarId)
                    intent.putExtra("userEmail",userEmail)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(calendar: ShareCalendar) {
            calendarImage.setImageResource(calendar.shareCalendarImage)
            calendarTitle.text=calendar.shareCalendarTitle
            calendarCategory.text=calendar.shareCalendarCategory
        }
    }
}