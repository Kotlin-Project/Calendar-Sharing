package com.example.kotlincalendar.Calendar

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.R
import java.util.Calendar
import java.util.Date

//원래 Date클래스를 따로 만들어야 하지만 여기는 단순한 String이기 때문에 사용하지 않음
class Calendar_Adapter(private val dayList:ArrayList<Date>, val userEmail: String?) :
    RecyclerView.Adapter<Calendar_Adapter.ItemViewHolder>(){
    //클릭 날짜
    private var selectedDate: Date? = null

    //각 아이템의 뷰를 담당하는 클래스
    //xml의 day_text를 참조
    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val dayText=itemView.findViewById<TextView>(R.id.day_text)
        val dayLayout=itemView.findViewById<LinearLayout>(R.id.day_Layout)
        }

    //아이템 뷰를 생성하고 뷰홀더객체를 생성함
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ItemViewHolder{
        val view=LayoutInflater.from(parent.context)
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
        val selectYear=CalendarUtil.selectedDate.get(Calendar.YEAR)
        val selectMonth=CalendarUtil.selectedDate.get(Calendar.MONTH)
        val selectDay=CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH)
        var todayDate=Calendar.getInstance()

        if(mYear==selectYear && mMonth==selectMonth){
            holder.dayText.setTextColor(Color.parseColor("#000000"))
            //오늘날짜 색상 변경
            if(mYear == todayDate.get(Calendar.YEAR) && mMonth == todayDate.get(Calendar.MONTH) && mDay == todayDate.get(Calendar.DAY_OF_MONTH)){
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

        //아이템 클릭
        val itemDate = dayList[position]
        val isItemSelected = itemDate == selectedDate
        holder.dayLayout.setBackgroundResource(if (isItemSelected) R.drawable.calendar_selected else 0)
        holder.itemView.setOnClickListener {
            if(isItemSelected){
                //테투리 있는 상태에서 클릭 시 일정추가 액티비티로 이동
                //클릭 Date를 밀리초로 변환 후 일정추가 액티비티로 전달
                val intent = Intent(holder.itemView.context,Calendar_Schedule_management::class.java)
                intent.putExtra("selectedDate",selectedDate?.time)
                intent.putExtra("user_email",userEmail)
                holder.itemView.context.startActivity(intent)
            }else {
                selectedDate = if (isItemSelected) null else itemDate
                notifyDataSetChanged()
            }
        }
/*        val itemDate = dayList[position]
        holder.itemView.setOnClickListener {
            if (itemDate == selectedDate) {
                selectedDate = null
            } else {
                selectedDate = itemDate
            }
            notifyDataSetChanged()
        }
        if (itemDate == selectedDate) {
            holder.dayLayout.setBackgroundResource(R.drawable.calendar_selected)
        } else {
            holder.dayLayout.background = null
        }*/
    }
    override fun getItemCount():Int{
        return dayList.size
    }
}