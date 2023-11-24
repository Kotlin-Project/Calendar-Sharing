package com.example.kotlincalendar.FriendCalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityFriendDetailScheduleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private var mBinding: ActivityFriendDetailScheduleBinding? = null
private val binding get() = mBinding!!
private var db: AppDatabase? = null

class FriendDetailSchedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFriendDetailScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)
        val scheduleId = intent.getIntExtra("scheduleId", -1)

        val TitleText=binding.TitleDetail
        val selectedLocalTime=binding.LocalDateDetail
        val startTimeLocal=binding.starTextBtnDetail
        val finshTimeLocal=binding.finshTextBtnDetail
        val memoText=binding.memoTextDetail

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
    }
}