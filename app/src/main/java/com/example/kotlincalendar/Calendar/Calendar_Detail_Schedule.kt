package com.example.kotlincalendar.Calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarDetailScheduleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private var mBinding: ActivityCalendarDetailScheduleBinding? = null
private val binding get() = mBinding!!
private var db: AppDatabase? = null

class Calendar_Detail_Schedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCalendarDetailScheduleBinding.inflate(layoutInflater);
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)
        val scheduleId = intent.getIntExtra("scheduleId", -1)

        val TitleText=findViewById<TextView>(R.id.Title_detail)
        val selectedLocalTime=findViewById<TextView>(R.id.LocalDate_detail)
        val startTimeLocal=findViewById<TextView>(R.id.star_text_btn_detail)
        val finshTimeLocal=findViewById<TextView>(R.id.finsh_text_btn_detail)
        val memoText=findViewById<TextView>(R.id.memoText_detail)

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