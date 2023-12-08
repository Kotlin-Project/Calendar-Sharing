package com.example.kotlincalendar.ShareCalendar

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.Calendar.CalendarUtil
import com.example.kotlincalendar.Calendar.PreviewSchedule_Adapter
import com.example.kotlincalendar.Entity.ShareCalendarSchedule
import com.example.kotlincalendar.Entity.UserCalendar
import com.example.kotlincalendar.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ShareCalendarAdapter(private val shareScheduleList:List<ShareCalendarSchedule>, private val dayList:ArrayList<Date>, val userEmail: String?, val shareCalendarId:String?) :
    RecyclerView.Adapter<ShareCalendarAdapter.ItemViewHolder>(){
    //클릭 날짜

    private var selectedDate: Date? = null

    //각 아이템의 뷰를 담당하는 클래스
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dayText=itemView.findViewById<TextView>(R.id.day_text)
        val dayLayout=itemView.findViewById<LinearLayout>(R.id.day_Layout)
        val preview=itemView.findViewById<RecyclerView>(R.id.previewRecyclerView)
    }

    //아이템 뷰를 생성하고 뷰홀더객체를 생성함
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ItemViewHolder{
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_day, parent, false)

        return ItemViewHolder(view)
    }

    //뷰홀더와 데이터를 바인딩
    //아이템 뷰를 업데이트 할 떄 호출
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //날짜 변수에 in
        var monthDate=dayList[holder.adapterPosition]
        //초기화
        var dateCalendar= Calendar.getInstance()
        //날짜 캘린더에 in
        dateCalendar.time=monthDate
        //캘린더 값 날짜 변수에 in
        var dayNo=dateCalendar.get(Calendar.DAY_OF_MONTH)
        holder.dayText.text=dayNo.toString()

        //메인화면에서 받아온 Date
        val mYear=dateCalendar.get(Calendar.YEAR)
        val mMonth=dateCalendar.get(Calendar.MONTH)
        val mDay=dateCalendar.get(Calendar.DAY_OF_MONTH)

        //캘린더에서 공유 받고 있는 캘린더Date 즉 현재 날짜
        val selectYear= CalendarUtil.selectedDate.get(Calendar.YEAR)
        val selectMonth= CalendarUtil.selectedDate.get(Calendar.MONTH)
        val selectDay= CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH)
        var todayDate= Calendar.getInstance()

        if(mYear==selectYear && mMonth==selectMonth){
            holder.dayText.setTextColor(Color.parseColor("#000000"))
            //오늘날짜 색상 변경
            if(mYear == todayDate.get(Calendar.YEAR) && mMonth == todayDate.get(Calendar.MONTH) && mDay == todayDate.get(
                    Calendar.DAY_OF_MONTH)){
                //원형으로 현재 날짜 색칠
                val shapeDrawable = GradientDrawable()
                shapeDrawable.shape = GradientDrawable.OVAL
                shapeDrawable.setColor(Color.LTGRAY)
                holder.dayText.background=shapeDrawable
            }
            //주말 색상 변경
            if((position+1)%7==0){
                holder.dayText.setTextColor(Color.BLUE)
            }else if(position==0||position%7==0){
                holder.dayText.setTextColor(Color.RED)
            }
        }else{//다른 달 날짜 색상 연하게
            holder.dayText.setTextColor(Color.parseColor("#B4B4B4"))
            if((position+1)%7==0){
                holder.dayText.setTextColor(Color.parseColor("#6495ED"))
            }else if(position==0||position%7==0){
                holder.dayText.setTextColor(Color.parseColor("#FFB4B4"))
            }
        }
        //작업중 코드--------------------------------------------------

                val preItemDate = dayList[position]
                // 선택된 날짜에 해당하는 일정 목록을 업데이트합니다.
                val selectedDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(preItemDate)
                val filteredScheduleList = shareScheduleList.filter { it.shareScheduleLocalDate.toString() == selectedDateString }

                val previewAdapter = SharePreViewAdapter(filteredScheduleList)
                holder.preview.layoutManager = LinearLayoutManager(holder.itemView.context)
                holder.preview.adapter = previewAdapter


        //작업중 코드--------------------------------------------------

        //아이템 클릭
        val itemDate = dayList[position]
        val isItemSelected = itemDate == selectedDate
        holder.dayLayout.setBackgroundResource(if (isItemSelected) R.drawable.calendar_selected else 0)


        holder.preview.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                holder.itemView.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })

        holder.preview.setOnClickListener {
            if (selectedDate != itemDate) {
                selectedDate = itemDate
                notifyDataSetChanged()
            }
            holder.dayLayout.performClick()
        }

        //아이템 클릭 리스너
        holder.itemView.setOnClickListener {
            if(isItemSelected){
                //테투리 있는 상태에서 클릭 시 일정추가 액티비티로 이동
                //클릭 Date를 밀리초로 변환 후 일정추가 액티비티로 전달
                val intent = Intent(holder.itemView.context, ShareScheduleManagement::class.java)
                intent.putExtra("selectedDate",selectedDate?.time)
                intent.putExtra("user_Email",userEmail)
                intent.putExtra("shareCalendarId",shareCalendarId)
                holder.itemView.context.startActivity(intent)
            }else {
                selectedDate = if (isItemSelected) null else itemDate
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount():Int{
        return dayList.size
    }
}