package com.example.kotlincalendar.ShareCalendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.Entity.ShareCalendarSchedule
import com.example.kotlincalendar.Entity.UserCalendar
import com.example.kotlincalendar.R

class SharePreViewAdapter(private val scheduleList: List<ShareCalendarSchedule>) : RecyclerView.Adapter<SharePreViewAdapter.PreviewViewHolder>() {

    //최대 4개 아이템까지
    private val limitedItemList = scheduleList.take(4)

    class PreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText = itemView.findViewById<TextView>(R.id.Preview_title_presc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_schedule_item, parent, false)
        return PreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.titleText.text = schedule.shareScheduleTitle
        val backgroundColor = getColorForColorName(schedule.shareScheduleColor)
        holder.titleText.setBackgroundColor(Color.parseColor(backgroundColor))
    }

    override fun getItemCount(): Int {
        return limitedItemList.size
    }

    private fun getColorForColorName(colorName: String): String {
        return when (colorName) {
            "빨간색" -> "#80FF0000"
            "파란색" -> "#800000FF"
            "초록색" -> "#80008000"
            "보라색" -> "#80800080"
            else -> "#80FF0000" // 기본값 설정
        }
    }
}