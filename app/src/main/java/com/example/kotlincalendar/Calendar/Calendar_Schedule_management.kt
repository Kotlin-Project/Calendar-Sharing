package com.example.kotlincalendar.Calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleManagementBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

private var mBinding: ActivityCalendarScheduleManagementBinding? = null
private val binding get() = mBinding!!

class Calendar_Schedule_management : AppCompatActivity() {
    private lateinit var scheduleAdapter: Calendar_Schedule_Adapter
    //private lateinit var userCalendarDao: UserCalendarDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCalendarScheduleManagementBinding.inflate(layoutInflater);
        setContentView(binding.root)
        var db = AppDatabase.getInstance(this)

        //선택날짜 가져오기
        val selectedDateMillis=intent.getLongExtra("selectedDate",0)
        val userEmail = intent.getStringExtra("user_Email")
        val selectedDate= Date(selectedDateMillis)
        val localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val dateFormatter = SimpleDateFormat("yyyy년 MM월 dd일 \nEEEEE요일", Locale.getDefault())
        val formattedDate = dateFormatter.format(selectedDate)

        //Rayout연결
        val select_Date=binding.selectedDateSchedule
        val add_btn=binding.plusBtnSchedule
        val schedule_List=binding.scheduleList
        select_Date.text=formattedDate

        //-----구현-----
        //일정 추가 액티비티로 이동
        add_btn.setOnClickListener{
            val intent = Intent(this, Calendar_Schedule_Add::class.java)
            intent.putExtra("selected_Date",selectedDateMillis)
            intent.putExtra("user_Email", userEmail)
            startActivity(intent)
        }

        //Adapter초기화 및 Adapter연결
        scheduleAdapter = Calendar_Schedule_Adapter(emptyList())
        schedule_List.layoutManager=LinearLayoutManager(this)
        schedule_List.adapter = scheduleAdapter
        GlobalScope.launch(Dispatchers.IO) {
            var scheduleList_=db!!.userCalendarDao().getScheduleForUser(userEmail,localDate)
            withContext(Dispatchers.Main){
                scheduleAdapter.submitList(scheduleList_)
            }
        }
    }
}