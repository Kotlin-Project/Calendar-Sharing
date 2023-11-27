package com.example.kotlincalendar.Calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarDetailScheduleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

private var mBinding: ActivityCalendarDetailScheduleBinding? = null
private val binding get() = mBinding!!
private var db: AppDatabase? = null
private var scheduleId = -1
class Calendar_Detail_Schedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCalendarDetailScheduleBinding.inflate(layoutInflater);
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)
        scheduleId = intent.getIntExtra("scheduleId", -1)

        val TitleText=binding.TitleDetail
        val selectedLocalTime=binding.LocalDateDetail
        val startTimeLocal=binding.starTextBtnDetail
        val finshTimeLocal=binding.finshTextBtnDetail
        val memoText=binding.memoTextDetail
        val deleteBtn=binding.deleteBtn
        val editBtn=binding.editBtn

        GlobalScope.launch(Dispatchers.IO) {
            val schedule = db?.userCalendarDao()!!.getCalendarItemById(scheduleId)
            withContext(Dispatchers.Main) {
                val selectedSchedule = schedule[0]
                TitleText.text=selectedSchedule.Schedule_Title
                selectedLocalTime.text=selectedSchedule.Schedule_LocalDate.toString()
                startTimeLocal.text=selectedSchedule.Schedule_Start.toString()
                finshTimeLocal.text=selectedSchedule.Schedule_End.toString()
                memoText.text=selectedSchedule.Schedule_Memo
            }
        }
        deleteBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val schedule = db?.userCalendarDao()!!.getCalendarItemById(scheduleId)
                db?.userCalendarDao()!!.deleteSchedule(schedule[0])
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Calendar_Detail_Schedule, "삭제완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        editBtn.setOnClickListener {
            val intent = Intent(this, CalendarScheduleEdit::class.java)
            intent.putExtra("scheduleItem", scheduleId)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        refreshScheduleDetails()
    }
    private fun refreshScheduleDetails() {
        GlobalScope.launch(Dispatchers.IO) {
            val schedule = db?.userCalendarDao()?.getCalendarItemById(scheduleId)
            withContext(Dispatchers.Main) {
                val selectedSchedule = schedule?.getOrNull(0) // null 또는 empty인 경우 처리
                selectedSchedule?.let {
                    binding.TitleDetail.text = it.Schedule_Title
                    binding.LocalDateDetail.text = it.Schedule_LocalDate.toString()
                    binding.starTextBtnDetail.text = it.Schedule_Start.toString()
                    binding.finshTextBtnDetail.text = it.Schedule_End.toString()
                    binding.memoTextDetail.text = it.Schedule_Memo
                }
            }
        }
    }
}