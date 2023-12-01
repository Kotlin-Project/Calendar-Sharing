package com.example.kotlincalendar.Calendar

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.kotlincalendar.Dao.UserDao
import com.example.kotlincalendar.FriendList.Frd_list
import com.example.kotlincalendar.FriendList.Frd_management
import com.example.kotlincalendar.Friend_list
import com.example.kotlincalendar.Login
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarBinding
import com.example.kotlincalendar.dialog_status
import com.example.kotlincalendar.my_page
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.io.File
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
        val navigationView = binding.navigationView
        navigationView.inflateHeaderView(R.layout.navigation_header) // 헤더 레이아웃 설정

        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name_header)
        val statusTextView = headerView.findViewById<TextView>(R.id.statusText)
        val userProfileImg = headerView.findViewById<ImageView>(R.id.userProfile)

        CoroutineScope(Dispatchers.IO).launch {
            val userDataDB = db?.userDao()?.getUserId(userEmail)

            // Check if userDataDB is not null and contains at least one item
            if (userDataDB != null && userDataDB.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    val userDataList = userDataDB[0]
                    val userNameData = userDataList.Name
                    val userStatusData = userDataList.SubTitle
                    val userProfileData = userDataList.Profile_img

                    userNameTextView.text = userNameData
                    statusTextView.text = userStatusData

                    //Log.d("UserData","Profile_img: $userProfileData")

                    // Use the correct activity reference (replace YourCalendarMainActivity with the actual name of your activity)
                    Glide.with(this@CalendarMain)
                        .load(userProfileData)
                        .into(userProfileImg)
                }
            } else {
                // Handle the case where userDataDB is null or empty
                // For example, set default values or show an error message
            }
        }



        statusTextView.setOnClickListener {
            showCustomDialog(this)
        }

        //로그아웃 기능
        val logoutBtn = headerView.findViewById<Button>(R.id.logout_Btn)
        logoutBtn.setOnClickListener {
            Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        menubtn.setOnClickListener {
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


        //-------------------캘린더 구현-------------------------------------------
        GlobalScope.launch(Dispatchers.IO) {
            val userName_ = db!!.userDao().getUserNameByEmail(userEmail)
            withContext(Dispatchers.Main){
//                val userDB = userName_!![0]
                userName.text="${userName_}님의 캘린더"
//                userNameTextView.text ="${userName_}"
////                statusTextView.text = "${userDB.SubTitle}"
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

    private fun showCustomDialog(context : Context) {
        val statusDialog = Dialog(context)
        statusDialog.setContentView(R.layout.activity_dialog_status)
        val statusEditText =  statusDialog.findViewById<EditText>(R.id.statusEditText)
        val dialogBackBtn = statusDialog.findViewById<Button>(R.id.backBtn)
        val dialogChangeBtn = statusDialog.findViewById<Button>(R.id.conformBtn)
        var dialogDB = AppDatabase.getInstance(this)
        val statusUserEmail = intent.getStringExtra("user_email")
        CoroutineScope(Dispatchers.IO).launch {
            val userStatusDB = dialogDB?.userDao()!!.getUserId(statusUserEmail)
            withContext(Dispatchers.Main) {
                val userDB = userStatusDB[0]
                val statusDialog = userDB.SubTitle

                statusEditText.setText(statusDialog)
            }
        }

        dialogBackBtn.setOnClickListener {
            statusDialog.dismiss()
        }

        dialogChangeBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val userStatusDB = dialogDB?.userDao()!!.getUserId(statusUserEmail)
                withContext(Dispatchers.Main) {
                    val userDB = userStatusDB[0]
                    val newStatus = statusEditText.text.toString()
                    val updateResult = updateUserStatus(userDB.Email, newStatus)

                    if(updateResult) {
                        statusDialog.dismiss()
                        //새로고침..?
                        finish()
                        overridePendingTransition(0, 0)
                        val intent = getIntent()
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    } else {
                        showToast("변경 실패")
                    }
                }
            }
        }
        statusDialog.show()
    }
    private fun updateUserStatus(statusUserEmail: String, newStatus: String): Boolean{
        return try {
            var dialogDB = AppDatabase.getInstance(this)
            CoroutineScope(Dispatchers.IO).launch {
                dialogDB?.userDao()?.updateStatus(statusUserEmail, newStatus)
            }
            true // 변경 성공
        } catch (e: Exception) {
            false // 변경 실패
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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