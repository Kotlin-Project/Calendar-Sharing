package com.example.kotlincalendar.Calendar

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.UserCalendar

class Calendar_Schedule_Adapter(private var scheduleList: List<UserCalendar>) :
    RecyclerView.Adapter<Calendar_Schedule_Adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.Title_scitem)
        val startTimeTextView: TextView = itemView.findViewById(R.id.startTime_scitem)
        val endTimeTextView: TextView = itemView.findViewById(R.id.finsgTime_scitem)
        val colorView: View = itemView.findViewById(R.id.Color_scitem)
        val imagealarm:ImageView=itemView.findViewById(R.id.imageView)
        init {
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
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = scheduleList[position]

        holder.titleTextView.text = currentItem.Schedule_Title
        holder.startTimeTextView.text = currentItem.Schedule_Start.toString()
        holder.endTimeTextView.text = currentItem.Schedule_End.toString()

        val backgroundColor = getColorForColorName(currentItem.Schedule_Color)
        holder.colorView.setBackgroundColor(backgroundColor)

        if(currentItem.alarm=="설정 하지 않음"){
            holder.imagealarm.visibility = View.GONE
        }else{
            holder.imagealarm.visibility= View.VISIBLE
        }
    }

    fun submitList(list: List<UserCalendar>) {
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