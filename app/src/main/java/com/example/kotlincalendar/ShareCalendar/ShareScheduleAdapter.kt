package com.example.kotlincalendar.ShareCalendar

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.Calendar.Calendar_Detail_Schedule
import com.example.kotlincalendar.Calendar.Calendar_Schedule_Adapter
import com.example.kotlincalendar.Entity.ShareCalendarSchedule
import com.example.kotlincalendar.Entity.UserCalendar
import com.example.kotlincalendar.R

class ShareScheduleAdapter(private var scheduleList: List<ShareCalendarSchedule>) :
    RecyclerView.Adapter<ShareScheduleAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.scheduleTitle)
        val writerUser:TextView=itemView.findViewById(R.id.scheduleWriteUser)
        val startTimeTextView: TextView = itemView.findViewById(R.id.startTime)
        val endTimeTextView: TextView = itemView.findViewById(R.id.finshTime)
        val colorView: View = itemView.findViewById(R.id.selectedColor)
        val imagealarm: ImageView =itemView.findViewById(R.id.alarmImage)
        val writeUser:TextView=itemView.findViewById(R.id.scheduleWriteUser)
/*        init {
            // 아이템 클릭 시 상세 정보 화면으로 이동
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedItem = scheduleList[position]
                    val intent = Intent(itemView.context, Calendar_Detail_Schedule::class.java)
                    intent.putExtra("scheduleId", selectedItem.Schedule_ID)
                    itemView.context.startActivity(intent)
                }
            }
        }*/
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sharescheduleitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = scheduleList[position]

        holder.titleTextView.text = currentItem.shareScheduleTitle
        holder.startTimeTextView.text = currentItem.shareScheduleStart.toString()
        holder.endTimeTextView.text = currentItem.shareScheduleEnd.toString()
        holder.writeUser.text=currentItem.userEmail

        val backgroundColor = getColorForColorName(currentItem.shareScheduleColor)
        holder.colorView.setBackgroundColor(backgroundColor)

        if(currentItem.shareAlarm=="설정 하지 않음"){
            holder.imagealarm.visibility = View.GONE
        }else{
            holder.imagealarm.visibility= View.VISIBLE
        }
    }

    fun submitList(list: List<ShareCalendarSchedule>) {
        scheduleList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
    private fun getColorForColorName(colorName: String): Int {
        return when (colorName) {
            "빨간색" -> Color.RED
            "파란색" -> Color.BLUE
            "초록색" -> Color.GREEN
            "보라색" -> Color.MAGENTA
            else -> Color.BLACK // 기본값 설정
        }
    }

}