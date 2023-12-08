package com.example.kotlincalendar.ShareCalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarSettingBinding
import com.example.kotlincalendar.databinding.ActivityShareScheduleEditBinding

class ShareCalendarSetting : AppCompatActivity() {
    private var mBinding: ActivityShareCalendarSettingBinding? = null
    private val binding get() = mBinding!!
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityShareCalendarSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}