package com.example.kotlincalendar.Calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.kotlincalendar.R
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleManagementBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

private var mBinding: ActivityCalendarScheduleManagementBinding? = null
private val binding get() = mBinding!!

class Calendar_Schedule_management : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCalendarScheduleManagementBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val selectedDateMillis=intent.getLongExtra("selectedDate",0)
        val selectedDate= Date(selectedDateMillis)
        val localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val dateFormatter = SimpleDateFormat("yyyy년 MM월 dd일 \nEEEEE요일", Locale.getDefault())
        val formattedDate = dateFormatter.format(selectedDate)

        val select_Date=findViewById<TextView>(R.id.selected_Date_Schedule)
        val add_btn=findViewById<Button>(R.id.plus_btn_schedule)
        select_Date.text=formattedDate

        add_btn.setOnClickListener{
            val intent = Intent(this, Calendar_Schedule_Add::class.java)
        intent.putExtra("selected_Date",selectedDateMillis)
        startActivity(intent)
        }

    }
}