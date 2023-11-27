package com.example.kotlincalendar.Calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
private lateinit var db: AppDatabase
class Calendar_Schedule_management : AppCompatActivity() {
    private lateinit var scheduleAdapter: Calendar_Schedule_Adapter
    private var selectedDateMillis: Long = 0
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCalendarScheduleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)!!

        // 인텐트에서 선택된 날짜와 이메일을 가져옴
        selectedDateMillis = intent.getLongExtra("selectedDate", 0)
        userEmail = intent.getStringExtra("user_Email")

        // 선택된 날짜를 포맷하여 화면에 표시
        val formattedDate = formatDate(selectedDateMillis)
        setupUI(formattedDate)

        // 일정 추가 버튼 설정
        setupAddButton(selectedDateMillis, userEmail)

        // 스케줄 리스트 설정
        setupScheduleList(db, userEmail, selectedDateMillis)
    }

    // 날짜 포맷 함수
    private fun formatDate(selectedDateMillis: Long): String {
        val selectedDate = Date(selectedDateMillis)
        val dateFormatter = SimpleDateFormat("yyyy년 MM월 dd일 \nEEEEE요일", Locale.getDefault())
        return dateFormatter.format(selectedDate)
    }

    // UI 설정 함수
    private fun setupUI(formattedDate: String) {
        binding.selectedDateSchedule.text = formattedDate
    }

    // 일정 추가 버튼 설정 함수
    private fun setupAddButton(selectedDateMillis: Long, userEmail: String?) {
        binding.plusBtnSchedule.setOnClickListener {
            val intent = Intent(this, Calendar_Schedule_Add::class.java)
            intent.putExtra("selected_Date", selectedDateMillis)
            intent.putExtra("user_Email", userEmail)
            startActivity(intent)
        }
    }

    // 스케줄 리스트 설정 함수
    private fun setupScheduleList(db: AppDatabase?, userEmail: String?, selectedDateMillis: Long) {
        scheduleAdapter = Calendar_Schedule_Adapter(emptyList())
        binding.scheduleList.layoutManager = LinearLayoutManager(this)
        binding.scheduleList.adapter = scheduleAdapter

        // 선택된 날짜에 해당하는 스케줄을 가져와 어댑터에 연결
        //val db = AppDatabase.getInstance(this)!!
        GlobalScope.launch(Dispatchers.IO) {
            val localDate = Date(selectedDateMillis).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val scheduleList = db!!.userCalendarDao().getScheduleForUser(userEmail, localDate)
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
        GlobalScope.launch(Dispatchers.IO) {
            val localDate = Date(selectedDateMillis).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val scheduleList = db?.userCalendarDao()?.getScheduleForUser(userEmail, localDate)
            withContext(Dispatchers.Main) {
                scheduleAdapter.submitList(scheduleList ?: emptyList())
            }
        }
    }
}