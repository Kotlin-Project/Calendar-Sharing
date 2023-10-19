package com.example.kotlincalendar.Calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleManagementBinding

private var mBinding: ActivityCalendarScheduleManagementBinding? = null
private val binding get() = mBinding!!

class Calendar_Schedule_management : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCalendarScheduleManagementBinding.inflate(layoutInflater);
        setContentView(binding.root)
    }
}