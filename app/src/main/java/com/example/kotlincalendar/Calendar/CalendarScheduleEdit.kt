package com.example.kotlincalendar.Calendar

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.Entity.UserCalendar
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

private var mBinding: ActivityCalendarScheduleEditBinding? = null
private val binding get() = mBinding!!
private var db: AppDatabase? = null

class CalendarScheduleEdit : AppCompatActivity() {

    private val colors = arrayOf("빨간색", "파란색", "초록색", "보라색")
    private val clockTime = arrayOf("설정 하지 않음", "10분", "30분", "1시간")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCalendarScheduleEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scheduleId = intent.getIntExtra("scheduleItem", -1)
        val userEmail = intent.getStringExtra("user_Email")

        initializeViews(scheduleId)

        binding.saveBtnScadd.setOnClickListener {
            editSchedule(scheduleId)
        }
        binding.cancelBtnScadd.setOnClickListener {
            finish()
        }
    }

    private fun initializeViews(scheduleId:Int) {
        db = AppDatabase.getInstance(this)


        val selectedDateItem = binding.selectedDateEdit
        val titleItem = binding.titleEdit
        val startTimeItem = binding.starTextBtnEdit
        val finishTimeItem = binding.finshTextBtnEdit
        val memoItem = binding.memoEditText

        setupSpinners()

        GlobalScope.launch(Dispatchers.IO) {
            val schedule = db?.userCalendarDao()!!.getCalendarItemById(scheduleId)
            withContext(Dispatchers.Main) {
                val selectedSchedule = schedule[0]

                titleItem.setText(selectedSchedule.Schedule_Title)
                selectedDateItem.text = selectedSchedule.Schedule_LocalDate.toString()
                startTimeItem.text = selectedSchedule.Schedule_Start.toString()
                finishTimeItem.text = selectedSchedule.Schedule_End.toString()
                memoItem.setText(selectedSchedule.Schedule_Memo)

                setupColorSpinner(selectedSchedule.Schedule_Color)
                setupClockSpinner(selectedSchedule.alarm)
            }
        }

        setupTimePickerButton(startTimeItem)
        setupTimePickerButton(finishTimeItem)
    }

    private fun setupSpinners() {
        setupSpinner(binding.ColorSpinner, colors)
        setupSpinner(binding.clockSpinner, clockTime)
    }

    private fun setupSpinner(spinner: Spinner, items: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupColorSpinner(selectedColor: String) {
        val indexOfSelectedColor = colors.indexOf(selectedColor)
        binding.ColorSpinner.setSelection(indexOfSelectedColor)
    }

    private fun setupClockSpinner(selectedClock: String) {
        val indexOfSelectedClock = clockTime.indexOf(selectedClock)
        binding.clockSpinner.setSelection(indexOfSelectedClock)
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

    private fun editSchedule(scheduleId:Int){
        var titleEdit=binding.titleEdit.getText().toString()
        var startTimeEdit= LocalTime.parse(binding.starTextBtnEdit.text.toString(), DateTimeFormatter.ofPattern("HH:mm"))
        var finishTimeEdit= LocalTime.parse(binding.finshTextBtnEdit.text.toString(), DateTimeFormatter.ofPattern("HH:mm"))
        var memoEdit=binding.memoEditText.getText().toString()
        var colorEdit=binding.ColorSpinner.selectedItem.toString()
        var clockEdit=binding.clockSpinner.selectedItem.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val schedule = db?.userCalendarDao()?.getCalendarItemById(scheduleId)
            if (schedule != null && schedule.isNotEmpty()) {
                val selectedScheduleEdit = schedule[0]

                selectedScheduleEdit.Schedule_Title = titleEdit
                selectedScheduleEdit.Schedule_Start = startTimeEdit
                selectedScheduleEdit.Schedule_End = finishTimeEdit
                selectedScheduleEdit.Schedule_Memo = memoEdit
                selectedScheduleEdit.Schedule_Color = colorEdit
                selectedScheduleEdit.alarm = clockEdit

                db?.userCalendarDao()?.updateSchedule(selectedScheduleEdit)
                finish()
            }
        }
    }
}