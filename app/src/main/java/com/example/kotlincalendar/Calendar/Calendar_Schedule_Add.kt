package com.example.kotlincalendar.Calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.kotlincalendar.R
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleAddBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

private var mBinding: ActivityCalendarScheduleAddBinding? = null
private val binding get() = mBinding!!

class Calendar_Schedule_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityCalendarScheduleAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //가져온 값
        val selectedDateMillis = intent.getLongExtra("selected_Date",0)
        val userEmail = intent.getStringExtra("user_email")
        //레이아웃 연결
        val colorSpinner: Spinner = findViewById(R.id.Color_spinner)
        val clockSpinner: Spinner=findViewById(R.id.clock_spinner)
        val select_Date=findViewById<TextView>(R.id.selected_Date_Add)
        val title_EditText=findViewById<EditText>(R.id.title_Add)
        val strat_Text_Btn=findViewById<TextView>(R.id.star_text_btn_Add)
        var finsh_Text_Btn=findViewById<TextView>(R.id.finsh_text_btn_Add)
        var memo_EditText=findViewById<EditText>(R.id.memo_EditText_Add)
        //날짜 정보 및 LocalDate 변환
        val selectedDate= Date(selectedDateMillis)
        val localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd (EEEEE)", Locale.getDefault())
        val formattedDate = dateFormatter.format(selectedDate)

        //스피너에 들어갈 데이터
        val colors = arrayOf("빨간색", "파란색", "초록색", "보라색")
        val clock_time=arrayOf("설정 하지 않음", "10분", "30분", "1시간")

        //컬러 스피너 객체 생성
        val colorAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, colors)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = colorAdapter

        //타임 스피너 객체 생성
        val timeAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, clock_time)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        clockSpinner.adapter = timeAdapter

        //구현
        select_Date.text=formattedDate
        strat_Text_Btn.setOnClickListener{

        }

    }
}