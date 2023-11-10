package com.example.kotlincalendar.Calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class CalendarMain : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater);
        setContentView(binding.root)

        var db = AppDatabase.getInstance(this)
        val userEmail = intent.getStringExtra("user_email")
        setMonthView(userEmail)

        val userName=binding.UserNameCamain
        val prebtn=binding.preBtn
        val nextbtn=binding.nextBtn
        val menubtn=binding.menuBtnCalendar

        //햄버거 메뉴
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.inflateHeaderView(R.layout.navigation_header) // 헤더 레이아웃 설정

        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name_header)

        menubtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        //-------------------캘린더 구현-------------------------------------------
        GlobalScope.launch(Dispatchers.IO) {
            val userName_=db!!.userDao().getUserNameByEmail(userEmail)
            withContext(Dispatchers.Main){
                userName.text="${userName_}님의 캘린더"
                userNameTextView.text ="${userName_}님 환영합니다"
            }
        }
        //왼쪽버튼 클릭시 현재 날짜에서 -1월
        prebtn.setOnClickListener{
            CalendarUtil.selectedDate.add(Calendar.MONTH,-1)
            setMonthView(userEmail)
        }
        //오른쪽버튼 클릭시 현재 날짜에서 +1월
        nextbtn.setOnClickListener{
            CalendarUtil.selectedDate.add(Calendar.MONTH,1)
            setMonthView(userEmail)
        }
    }
    //날짜를 표시
    private fun setMonthView(userEmail:String?){
        var db = AppDatabase.getInstance(this)
        //년 월 표시
        binding.monthText.text=monthYearFromDate(CalendarUtil.selectedDate)

        //날짜 생성 후 리스트에 넣기
        val dayList=dayInMonthArray()

        GlobalScope.launch(Dispatchers.IO) {
            var scheduleList_=db!!.userCalendarDao().getScheduleUser(userEmail)
            withContext(Dispatchers.Main){
                //어댑터 초기화
                val adapter = Calendar_Adapter(scheduleList_,dayList, userEmail)

                //레이아웃 설정(열 개수)
                var manager:RecyclerView.LayoutManager=GridLayoutManager(applicationContext,7)

                //레이아웃 적용
                binding.calendarDayView.layoutManager=manager

                //어댑터 적용
                binding.calendarDayView.adapter=adapter
            }
        }
    }

    //DateTimeFormatter 클래스를 사용하여 Date정보를 원하는 형식으로 정의
    //date.format = 날짜를 형식화한 문자열로 반환
    private fun monthYearFromDate(calendar: Calendar): String{
        var year=calendar.get(Calendar.YEAR)
        var month=calendar.get(Calendar.MONTH)+1

        return "${year}년 ${month}월"
    }

    //날짜 생성
    private fun dayInMonthArray():ArrayList<Date> {
        var dayList = ArrayList<Date>()

        var monthCalendar=CalendarUtil.selectedDate.clone() as Calendar

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