package com.example.kotlincalendar.ShareCalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.kotlincalendar.Entity.ShareCalendar
import com.example.kotlincalendar.FriendList.Frd_management
import com.example.kotlincalendar.Login
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarMainBinding
import com.example.kotlincalendar.my_page
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
        val userEmail = intent.getStringExtra("userEmail")
        setTitleView(shareCalendarId)
        setMonthView(shareCalendarId)
        menuFunction(userEmail)
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
    //햄버거 메뉴
    private fun menuFunction(userEmail:String?){
        drawerLayout = findViewById(R.id.drawer_layout)
        var db = AppDatabase.getInstance(this)
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

        binding.menuBtn.setOnClickListener {
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
    override fun onResume() {
        super.onResume()
        val shareCalendarId = intent.getStringExtra("shareCalendarId")
        setMonthView(shareCalendarId)
    }
}