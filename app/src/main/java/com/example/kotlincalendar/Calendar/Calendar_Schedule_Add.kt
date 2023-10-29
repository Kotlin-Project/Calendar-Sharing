package com.example.kotlincalendar.Calendar

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.database.UserCalendar
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
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
        val userEmail = intent.getStringExtra("user_Email")
        //레이아웃 연결
        val colorSpinner: Spinner = findViewById(R.id.Color_spinner)
        val clockSpinner: Spinner=findViewById(R.id.clock_spinner)
        val select_Date=findViewById<TextView>(R.id.selected_Date_Add)
        val title_EditText=findViewById<EditText>(R.id.title_Add)
        val start_Text_Btn=findViewById<TextView>(R.id.star_text_btn_Add)
        val finsh_Text_Btn=findViewById<TextView>(R.id.finsh_text_btn_Add)
        val memo_EditText=findViewById<EditText>(R.id.memo_EditText_Add)
        val cancel_btn=findViewById<Button>(R.id.cancel_btn_scadd)
        val save_btn=findViewById<Button>(R.id.save_btn_scadd)

        var db = AppDatabase.getInstance(this)

        //날짜 정보 및 LocalDate 변환
        val selectedDate= Date(selectedDateMillis)
        val localDate_ = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
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

        //--------구현--------
        select_Date.text=formattedDate
        start_Text_Btn.setOnClickListener{
            val cal = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                // 시간을 선택한 후 TextView에 표시
                val selectedTime = String.format("%02d:%02d", h, m)
                start_Text_Btn.text = selectedTime
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }
        finsh_Text_Btn.setOnClickListener{
            val cal = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                // 시간을 선택한 후 TextView에 표시
                val selectedTime = String.format("%02d:%02d", h, m)
                finsh_Text_Btn.text = selectedTime
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }

        cancel_btn.setOnClickListener{
            finish()
        }
        save_btn.setOnClickListener{
            val Title_=title_EditText.getText().toString()
            val Memo_=memo_EditText.getText().toString()
            val Color_=colorSpinner.selectedItem.toString()
            val SelectStartTime=start_Text_Btn.text.toString()
            val SelectFinshTime=finsh_Text_Btn.text.toString()
            val alarm_=clockSpinner.selectedItem.toString()

            //시간을 LocalTime으로 변환
            val selectedStartTime_ = if (SelectStartTime!="시작 시간") {
                val timeParts = SelectStartTime.split(":")
                LocalTime.of(timeParts[0].toInt(), timeParts[1].toInt())
            } else {
                LocalTime.of(9, 0)  //값이 없으면 0900으로
            }
            val selectedFinshTime_= if (SelectStartTime!="끝나는 시간") {
                val timeParts = SelectStartTime.split(":")
                LocalTime.of(timeParts[0].toInt(), timeParts[1].toInt())
            } else {
                LocalTime.of(9, 0)  //값이 없으면 0900으로
            }
            var newSchedule= UserCalendar(0,userEmail,Title_,Memo_,Color_,localDate_,selectedStartTime_,selectedFinshTime_,alarm_)
            GlobalScope.launch(Dispatchers.IO) {
                db!!.userCalendarDao().insertSchedule(newSchedule)
            }
            Toast.makeText(this,"일정 생성 완료",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}