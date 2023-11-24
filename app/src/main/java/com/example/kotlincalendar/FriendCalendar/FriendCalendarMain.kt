package com.example.kotlincalendar.FriendCalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.Calendar.CalendarMain
import com.example.kotlincalendar.Calendar.CalendarUtil
import com.example.kotlincalendar.FriendList.Frd_management
import com.example.kotlincalendar.Login
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarBinding
import com.example.kotlincalendar.my_page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date


class FriendCalendarMain : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var drawerLayout: DrawerLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater);
        setContentView(binding.root)

        var db = AppDatabase.getInstance(this)
        var friendEmail = intent.getStringExtra("friend_email")
        val userEmail=intent.getStringExtra("user_email")
        Log.d("FriendDetailActivity", "Friend Email: $friendEmail")

        val prebtnItem=binding.preBtn
        val nextbtnItem=binding.nextBtn
        val menubtnItem=binding.menuBtnCalendar


        displayName(db,friendEmail)
        setMonthView(friendEmail)

        menubtnItem.setOnClickListener{
            menuFunction(db,menubtnItem,userEmail)
        }
        prebtnItem.setOnClickListener{
            leftBtn(friendEmail)
        }
        nextbtnItem.setOnClickListener{
            rightBtn(friendEmail)
        }

    }

    //캘린더 이름 출력
    private fun displayName(db: AppDatabase?, friendEmail: String?){
        val userNameItem=binding.UserNameCamain

        GlobalScope.launch(Dispatchers.IO) {
            val userName =db!!.userDao().getUserNameByEmail(friendEmail)
            withContext(Dispatchers.Main){
                userNameItem.text="${userName}님의 캘린더"
            }
        }
    }
    //햄버거 메뉴
    private fun menuFunction(db: AppDatabase?,menubtnItem:ImageButton,userEmail:String?){
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = binding.navigationView
        navigationView.inflateHeaderView(R.layout.navigation_header) // 헤더 레이아웃 설정
        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name_header)
        GlobalScope.launch(Dispatchers.IO) {
            val userName =db!!.userDao().getUserNameByEmail(userEmail)
            withContext(Dispatchers.Main){
                userNameTextView.text ="${userName}"
            }
        }

        //로그아웃 기능
        val logoutBtn = headerView.findViewById<Button>(R.id.logout_Btn)
        logoutBtn.setOnClickListener {
            Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        menubtnItem.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.my_Calendar -> {
                    val intent = Intent(this, CalendarMain::class.java)
                    intent.putExtra("user_email", userEmail)
                    startActivity(intent)
                    true
                }
                R.id.menu_share_Calendar -> {
                    true
                }
                R.id.menu_friend -> {
                    val intent = Intent(this, Frd_management::class.java)
                    intent.putExtra("user_email",userEmail)
                    startActivity(intent)
                    true
                }
                R.id.menu_userChange -> {
                    val intent = Intent(this, my_page::class.java)
                    intent.putExtra("user_email", userEmail)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
    //prebtnItem클릭시 현재 날짜에서 -1월
    private fun leftBtn(friendEmail: String?){
        CalendarUtil.selectedDate.add(Calendar.MONTH,-1)
        setMonthView(friendEmail)
    }

    //nextbtnItem클릭시 현재 날짜에서 +1월
    private fun rightBtn(friendEmail: String?){
        CalendarUtil.selectedDate.add(Calendar.MONTH,+1)
        setMonthView(friendEmail)
    }

    //날짜를 표시
    private fun setMonthView(friendEmail:String?){
        var db = AppDatabase.getInstance(this)
        //년 월 표시
        binding.monthText.text=monthYearFromDate(CalendarUtil.selectedDate)

        //날짜 생성 후 리스트에 넣기
        val dayList=dayInMonthArray()

        GlobalScope.launch(Dispatchers.IO) {
            var scheduleList_=db!!.userCalendarDao().getScheduleUser(friendEmail)
            withContext(Dispatchers.Main){
                //어댑터 초기화
                val adapter = FriendCalendarAdapter(scheduleList_,dayList, friendEmail)

                //레이아웃 설정(열 개수)
                var manager: RecyclerView.LayoutManager= GridLayoutManager(applicationContext,7)

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