package com.example.kotlincalendar.FriendCalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincalendar.Calendar.Calendar_Schedule_Adapter
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityFriendScheduleManagementBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

private lateinit var scheduleAdapter: FriendScheduleAdapter

private var mBinding: ActivityFriendScheduleManagementBinding? = null
private val binding get() = mBinding!!

class FriendScheduleManagement : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFriendScheduleManagementBinding.inflate(layoutInflater);
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
        val select_Date= binding.selectedDateSchedule
        val schedule_List=binding.scheduleList
        select_Date.text=formattedDate

        //Adapter초기화 및 Adapter연결
        scheduleAdapter = FriendScheduleAdapter(emptyList())
        schedule_List.layoutManager= LinearLayoutManager(this)
        schedule_List.adapter = scheduleAdapter
        GlobalScope.launch(Dispatchers.IO) {
            var scheduleList_=db!!.userCalendarDao().getScheduleForUser(userEmail,localDate)
            withContext(Dispatchers.Main){
                scheduleAdapter.submitList(scheduleList_)
            }
        }
    }
}