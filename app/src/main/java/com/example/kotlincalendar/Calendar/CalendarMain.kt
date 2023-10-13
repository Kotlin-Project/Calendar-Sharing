package com.example.kotlincalendar.Calendar

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlincalendar.R
import com.example.kotlincalendar.databinding.ActivityCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar


//private val binding get() = mBinding!!

class CalendarMain : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendar: Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater);
        setContentView(binding.root)

        //현재날짜
        CalendarUtil.selectedDate=LocalDate.now()

        setMonthView()

        val prebtn=findViewById<ImageButton>(R.id.pre_btn)
        val nextbtn=findViewById<ImageButton>(R.id.next_btn)

        //왼쪽버튼 클릭시 현재 날짜에서 -1월
        prebtn.setOnClickListener{
            CalendarUtil.selectedDate=CalendarUtil.selectedDate.minusMonths(1)
            setMonthView()
        }
        //오른쪽버튼 클릭시 현재 날짜에서 +1월
        nextbtn.setOnClickListener{
            CalendarUtil.selectedDate=CalendarUtil.selectedDate.plusMonths(1)
            setMonthView()
        }
    }
    //날짜를 표시
    private fun setMonthView(){
        //년 월 표시
        binding.monthText.text=monthYearFromDate(CalendarUtil.selectedDate)

        //날짜 생성 후 리스트에 넣기
        val dayList=dayInMonthArray(CalendarUtil.selectedDate)

        //어댑터 초기화
        val adapter = Calendar_Adapter(dayList)

        //레이아웃 설정(열 개수)
        var manager:RecyclerView.LayoutManager=GridLayoutManager(applicationContext,7)

        //레이아웃 적용
        binding.calendarDayView.layoutManager=manager

        //어댑터 적용
        binding.calendarDayView.adapter=adapter
    }

    //DateTimeFormatter 클래스를 사용하여 Date정보를 원하는 형식으로 정의
    //date.format = 날짜를 형식화한 문자열로 반환
    private fun monthYearFromDate(date: LocalDate): String{
        var formatter= DateTimeFormatter.ofPattern("YYYY년 MM월")
        return date.format(formatter)
    }

    //날짜 생성
    private fun dayInMonthArray(date:LocalDate):ArrayList<LocalDate?> {
        var dayList = ArrayList<LocalDate?>()

        var yearMonth = YearMonth.from(date)

        val lastDay = yearMonth.lengthOfMonth()

        val firstDay = CalendarUtil.selectedDate.withDayOfMonth(1)

        var dayOfWeek = firstDay.dayOfWeek.value

        for(i in 1..42) {
            if (i <= dayOfWeek || i > (lastDay + dayOfWeek)) {
                dayList.add(null)
            } else {
                dayList.add(LocalDate.of(CalendarUtil.selectedDate.year,
                    CalendarUtil.selectedDate.monthValue,
                    i-dayOfWeek))
            }
        }
        return dayList
    }
}