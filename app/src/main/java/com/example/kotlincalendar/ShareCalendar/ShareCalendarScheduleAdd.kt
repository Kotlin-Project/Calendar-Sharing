package com.example.kotlincalendar.ShareCalendar

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.kotlincalendar.Entity.ShareCalendarSchedule
import com.example.kotlincalendar.Entity.UserCalendar
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleAddBinding
import com.example.kotlincalendar.databinding.ActivityShareCalendarScheduleAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ShareCalendarScheduleAdd : AppCompatActivity() {
    private var mBinding: ActivityShareCalendarScheduleAddBinding? = null
    private val binding get() = mBinding!!

    private val colors = arrayOf("빨간색", "파란색", "초록색", "보라색")
    private val clockTime = arrayOf("설정 하지 않음", "10분", "30분", "1시간")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =ActivityShareCalendarScheduleAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedDateMillis = intent.getLongExtra("selectedDate", 0)
        val userEmail = intent.getStringExtra("userEmail")!!
        val shareCalendarId = intent.getStringExtra("shareCalendarId")!!
        Log.d("ShareCalendarScheduleAdd", "selectedDate: $selectedDateMillis")
        Log.d("ShareCalendarScheduleAdd", "userEmail: $userEmail")
        Log.d("ShareCalendarScheduleAdd", "shareCalendarId: $shareCalendarId")

        setInitialValues(selectedDateMillis)
        setSpinners()
        setupTimePickerButton(binding.starTextBtn)
        setupTimePickerButton(binding.finshTextBtn)
        createShareSchedule(selectedDateMillis,userEmail,shareCalendarId)
    }
    private fun setInitialValues(selectedDateMillis: Long) {
        // 선택한 날짜 정보 설정
        val selectedDate = Date(selectedDateMillis)
        val localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd (EEEEE)", Locale.getDefault())
        val formattedDate = dateFormatter.format(selectedDate)

        // 선택한 날짜 설정
        binding.selectedDateAdd.text = formattedDate
    }

    private fun setSpinners() {
        //컬러 스피너 객체 생성
        val colorAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, colors)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.ColorSpinner.adapter = colorAdapter

        //타임 스피너 객체 생성
        val timeAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, clockTime)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.clockSpinner.adapter = timeAdapter
    }

    private fun setupTimePickerButton(textView: TextView) {
        textView.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                val selectedTime = String.format("%02d:%02d", h, m)
                textView.text = selectedTime
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }
    }

    private fun createShareSchedule(selectedDateMillis:Long,userEmail:String,shareCalendarId:String){
        binding.saveBtn.setOnClickListener {
            var db = AppDatabase.getInstance(this)
            val selectedDate = Date(selectedDateMillis)
            val localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            val shareTitleItem=binding.shareTitle.getText().toString()
            val shareMemoItem=binding.shareMemo.text.toString()
            val shareColor=binding.ColorSpinner.selectedItem.toString()
            val shareAlarm=binding.clockSpinner.selectedItem.toString()
            val shareSelectStartTime=binding.starTextBtn.text.toString()
            val shareSelectFinshTime=binding.finshTextBtn.text.toString()


            val selectedStartTime = if (shareSelectStartTime!="시작 시간") {
                val timeParts = shareSelectStartTime.split(":")
                LocalTime.of(timeParts[0].toInt(), timeParts[1].toInt())
            } else {
                LocalTime.of(8, 0)  //값이 없으면 0800으로
            }
            val selectedFinshTime= if (shareSelectFinshTime!="끝나는 시간") {
                val timeParts = shareSelectFinshTime.split(":")
                LocalTime.of(timeParts[0].toInt(), timeParts[1].toInt())
            } else {
                LocalTime.of(9, 0)  //값이 없으면 0900으로
            }
            Log.d("ShareCalendarScheduleAdd", "selectedDate: $shareTitleItem")
            Log.d("ShareCalendarScheduleAdd", "selectedDate: $shareSelectStartTime")
            Log.d("ShareCalendarScheduleAdd", "selectedDate: $selectedStartTime")
            Log.d("ShareCalendarScheduleAdd", "selectedDate: $selectedDateMillis")

            var newShareSchedule= ShareCalendarSchedule(0,shareCalendarId,userEmail,shareTitleItem,shareMemoItem,shareColor,localDate,selectedStartTime,selectedFinshTime,shareAlarm,)
            GlobalScope.launch(Dispatchers.IO) {
                db!!.shareCalendarScheduleDao().insertShareSchedule(newShareSchedule)
            }
        }

    }
}