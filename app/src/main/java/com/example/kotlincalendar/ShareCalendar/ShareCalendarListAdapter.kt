package com.example.kotlincalendar.ShareCalendar

import android.content.Context
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

        fun bind(calendar: ShareCalendar) {
            calendarImage.setImageResource(calendar.shareCalendarImage)
            calendarTitle.text=calendar.shareCalendarTitle
            calendarCategory.text=calendar.shareCalendarCategory
            //calendarTitle.text = calendar.title
            //calendarCategory.text = calendar.category
            // 여기서 이미지를 로드하거나 다른 필요한 작업을 수행할 수 있습니다.
        }
    }
}