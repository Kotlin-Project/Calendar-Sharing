package com.example.kotlincalendar.ShareCalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.Calendar.CalendarUtil
import com.example.kotlincalendar.Entity.ShareCalendar
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class ShareCalendarMain : AppCompatActivity() {
    private var mBinding: ActivityShareCalendarMainBinding? = null
    private val binding get() = mBinding!!
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityShareCalendarMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shareCalendarId=intent.getStringExtra("shareCalendarId")!!
        setTitleView(shareCalendarId)
        setMonthView(shareCalendarId)
        //왼쪽버튼 클릭시 현재 날짜에서 -1월
        binding.preBtn.setOnClickListener{
            CalendarUtil.selectedDate.add(Calendar.MONTH,-1)
            setMonthView(shareCalendarId)
        }
        //오른쪽버튼 클릭시 현재 날짜에서 +1월
        binding.nextBtn.setOnClickListener{
            CalendarUtil.selectedDate.add(Calendar.MONTH,1)
            setMonthView(shareCalendarId)
        }
    }

    private fun setTitleView(shareCalendarId:String){
        var db = AppDatabase.getInstance(this)
        GlobalScope.launch(Dispatchers.IO){
            val shareCalendar = db!!.shareCalendarDao()!!.getShareCalendar(shareCalendarId)
            val title = shareCalendar.shareCalendarTitle
            val category = shareCalendar.shareCalendarCategory
            withContext(Dispatchers.Main) {
                binding.shareCalendarTitle.text=title
                binding.shareCalendarCategory.text=category
            }
        }
    }

    private fun setMonthView(shareCalendarId:String?){
        val userEmail = intent.getStringExtra("userEmail")
        var db = AppDatabase.getInstance(this)
        //년 월 표시
        binding.monthText.text=monthYearFromDate(CalendarUtil.selectedDate)

        //날짜 생성 후 리스트에 넣기
        val dayList=dayInMonthArray()

        GlobalScope.launch(Dispatchers.IO) {
            var shareScheduleList=db!!.shareCalendarScheduleDao().getShareScheduleId(shareCalendarId)
            withContext(Dispatchers.Main){
                //어댑터 초기화
                val adapter = ShareCalendarAdapter(shareScheduleList,dayList, userEmail,shareCalendarId)
                //레이아웃 설정(열 개수)
                var manager: RecyclerView.LayoutManager= GridLayoutManager(applicationContext,7)
                //setTitleView()

                //레이아웃 적용
                binding.calendarDayView.layoutManager=manager

                //어댑터 적용
                binding.calendarDayView.adapter=adapter
            }
        }
    }

    private fun monthYearFromDate(calendar: Calendar): String{
        var year=calendar.get(Calendar.YEAR)
        var month=calendar.get(Calendar.MONTH)+1

        return "${year}년 ${month}월"
    }

    private fun dayInMonthArray():ArrayList<Date> {
        var dayList = ArrayList<Date>()

        var monthCalendar= CalendarUtil.selectedDate.clone() as Calendar

        //1일로 저장
        monthCalendar[Calendar.DAY_OF_MONTH]=1

        //해당 달의 1일의 요일
        val firstDayOfMonth=monthCalendar[Calendar.DAY_OF_WEEK]-1

        //요일 숫자만큼 이전 날짜로 설정
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)

        while(dayList.size<42){
            dayList.add(monthCalendar.time)

            //1일씩 증가
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }
        return dayList
    }
}