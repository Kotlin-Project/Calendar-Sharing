package com.example.kotlincalendar.ShareCalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincalendar.Calendar.Calendar_Schedule_Adapter
import com.example.kotlincalendar.Calendar.Calendar_Schedule_Add
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleManagementBinding
import com.example.kotlincalendar.databinding.ActivityShareScheduleManagementBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class ShareScheduleManagement : AppCompatActivity() {
    private val binding get() = mBinding!!
    private var mBinding: ActivityShareScheduleManagementBinding? = null
    private lateinit var db: AppDatabase

    private lateinit var scheduleAdapter: ShareScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityShareScheduleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)!!

        val selectedDateMillis = intent.getLongExtra("selectedDate", 0)
        val userEmail = intent.getStringExtra("user_Email")
        val shareCalendarId=intent.getStringExtra("shareCalendarId")!!
        Log.d("ShareCalendarCreate", "shareCalendarIdList--------ID: $selectedDateMillis")
        Log.d("ShareCalendarCreate", "shareCalendarIdList--------ID: $userEmail")
        Log.d("ShareCalendarCreate", "shareCalendarIdList--------ID: $shareCalendarId")


        // 선택된 날짜를 포맷하여 화면에 표시
        val formattedDate = formatDate(selectedDateMillis)
        setupUI(formattedDate)
        setupAddButton(selectedDateMillis,userEmail,shareCalendarId)
        setupScheduleList(db,userEmail,selectedDateMillis,shareCalendarId)
    }

    private fun formatDate(selectedDateMillis: Long): String {
        val selectedDate = Date(selectedDateMillis)
        val dateFormatter = SimpleDateFormat("yyyy년 MM월 dd일 \nEEEEE요일", Locale.getDefault())
        return dateFormatter.format(selectedDate)
    }
    private fun setupUI(formattedDate: String) {
        binding.selectedDateView.text = formattedDate
    }
    private fun setupAddButton(selectedDateMillis: Long, userEmail: String?,shareCalendarId:String?) {
        binding.plusBtnSchedule.setOnClickListener {
            val intent = Intent(this, ShareCalendarScheduleAdd::class.java).apply {
                putExtra("selectedDate", selectedDateMillis)
                putExtra("userEmail", userEmail)
                putExtra("shareCalendarId", shareCalendarId)
            }
            startActivity(intent)
        }
    }
    private fun setupScheduleList(db: AppDatabase?, userEmail: String?, selectedDateMillis: Long,shareCalendarId:String) {
        scheduleAdapter = ShareScheduleAdapter(emptyList())
        binding.scheduleList.layoutManager = LinearLayoutManager(this)
        binding.scheduleList.adapter = scheduleAdapter

        // 선택된 날짜에 해당하는 스케줄을 가져와 어댑터에 연결
        //val db = AppDatabase.getInstance(this)!!
        GlobalScope.launch(Dispatchers.IO) {
            val localDate = Date(selectedDateMillis).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val scheduleList = db!!.shareCalendarScheduleDao().getShareScheduleForUser(shareCalendarId, localDate)
            //val wirteUserName=db!!.
            withContext(Dispatchers.Main) {
                scheduleAdapter.submitList(scheduleList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 현재 화면이 다시 보여질 때마다 스케줄을 업데이트합니다.
        updateScheduleList()
    }

    // 스케줄 리스트 업데이트 함수
    private fun updateScheduleList() {
        val selectedDateMillis = intent.getLongExtra("selectedDate", 0)
        val userEmail = intent.getStringExtra("user_Email")
        val shareCalendarId=intent.getStringExtra("shareCalendarId")!!

        GlobalScope.launch(Dispatchers.IO) {
            val localDate = Date(selectedDateMillis).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val scheduleList = db!!.shareCalendarScheduleDao().getShareScheduleForUser(shareCalendarId, localDate)

            withContext(Dispatchers.Main) {
                scheduleAdapter.submitList(scheduleList)
            }
        }
    }
}