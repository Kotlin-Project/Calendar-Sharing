package com.example.kotlincalendar.Calendar

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.R
import java.time.LocalDate

//원래 Date클래스를 따로 만들어야 하지만 여기는 단순한 String이기 때문에 사용하지 않음
class Calendar_Adapter(private val dayList:ArrayList<LocalDate?>) :
    RecyclerView.Adapter<Calendar_Adapter.ItemViewHolder>(){

    //각 아이템의 뷰를 담당하는 클래스
    //xml의 day_text를 참조
    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            val dayText=itemView.findViewById<TextView>(R.id.day_text)
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
        //var dayList_=dayList[holder.adapterPosition]
        //holder.dayText.text=dayList_

        val today=LocalDate.now()
        val currentDate=dayList[holder.adapterPosition]

        //캘린더 빈칸 전달, 담달 날짜로 채우기
        if(currentDate==null)
            holder.dayText.text="a"
        else {
            holder.dayText.text = currentDate.dayOfMonth.toString()
        }
        //오늘날짜 색상 변경
        if(currentDate==today){
            //holder.itemView.setBackgroundColor(Color.LTGRAY)
            //원형으로 현재 날짜 색칠
            val shapeDrawable = GradientDrawable()
            shapeDrawable.shape = GradientDrawable.OVAL
            shapeDrawable.setColor(Color.LTGRAY)
            holder.dayText.background=shapeDrawable
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE)
        }


        //토,일 색상 변경
        if((position+1)%7==0){
            holder.dayText.setTextColor(Color.BLUE)
        }else if(position==0||position%7==0){
            holder.dayText.setTextColor(Color.RED)
        }
    }
    override fun getItemCount():Int{
        return dayList.size
    }
}