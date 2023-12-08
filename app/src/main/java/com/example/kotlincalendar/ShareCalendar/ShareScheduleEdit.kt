package com.example.kotlincalendar.ShareCalendar

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityCalendarScheduleEditBinding
import com.example.kotlincalendar.databinding.ActivityShareScheduleEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class ShareScheduleEdit : AppCompatActivity() {
    private var mBinding: ActivityShareScheduleEditBinding? = null
    private val binding get() = mBinding!!
    private var db: AppDatabase? = null

    private val colors = arrayOf("빨간색", "파란색", "초록색", "보라색")
    private val clockTime = arrayOf("설정 하지 않음", "10분", "30분", "1시간")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityShareScheduleEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editShareScheduleId = intent.getLongExtra("shareScheduleId", -1)
        val userEmail = intent.getStringExtra("user_Email")

        initializeViews(editShareScheduleId)

        binding.editBtn.setOnClickListener {
            editSchedule(editShareScheduleId)
        }
        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }

    private fun initializeViews(editShareScheduleId:Long) {
        db = AppDatabase.getInstance(this)


        val selectedDateItem = binding.selectedDateEdit
        val titleItem = binding.titleEdit
        val startTimeItem = binding.starTextBtnEdit
        val finishTimeItem = binding.finishTextBtnEdit
        val memoItem = binding.memoEditText

        setupSpinners()
        GlobalScope.launch(Dispatchers.IO) {
            val schedule = db?.shareCalendarScheduleDao()!!.getScheduleItemById(editShareScheduleId)
            withContext(Dispatchers.Main) {
                val selectedSchedule = schedule[0]

                titleItem.setText(selectedSchedule.shareScheduleTitle)
                selectedDateItem.text = selectedSchedule.shareScheduleLocalDate.toString()
                startTimeItem.text = selectedSchedule.shareScheduleStart.toString()
                finishTimeItem.text = selectedSchedule.shareScheduleEnd.toString()
                memoItem.setText(selectedSchedule.shareScheduleMemo)

                setupColorSpinner(selectedSchedule.shareScheduleColor)
                setupClockSpinner(selectedSchedule.shareAlarm)
            }
        }

        setupTimePickerButton(startTimeItem)
        setupTimePickerButton(finishTimeItem)
    }

    private fun setupSpinners() {
        setupSpinner(binding.colorSpinner, colors)
        setupSpinner(binding.clockSpinner, clockTime)
    }

    private fun setupSpinner(spinner: Spinner, items: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupColorSpinner(selectedColor: String) {
        val indexOfSelectedColor = colors.indexOf(selectedColor)
        binding.colorSpinner.setSelection(indexOfSelectedColor)
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

    private fun editSchedule(editShareScheduleId:Long){
        var titleEdit= binding.titleEdit.getText().toString()
        var startTimeEdit= LocalTime.parse(binding.starTextBtnEdit.text.toString(), DateTimeFormatter.ofPattern("HH:mm"))
        var finishTimeEdit= LocalTime.parse(binding.finishTextBtnEdit.text.toString(), DateTimeFormatter.ofPattern("HH:mm"))
        var memoEdit= binding.memoEditText.getText().toString()
        var colorEdit= binding.colorSpinner.selectedItem.toString()
        var clockEdit= binding.clockSpinner.selectedItem.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val schedule = db?.shareCalendarScheduleDao()!!.getScheduleItemById(editShareScheduleId)
            if (schedule != null && schedule.isNotEmpty()) {
                val selectedScheduleEdit = schedule[0]

                selectedScheduleEdit.shareScheduleTitle = titleEdit
                selectedScheduleEdit.shareScheduleStart = startTimeEdit
                selectedScheduleEdit.shareScheduleEnd = finishTimeEdit
                selectedScheduleEdit.shareScheduleMemo = memoEdit
                selectedScheduleEdit.shareScheduleColor = colorEdit
                selectedScheduleEdit.shareAlarm = clockEdit

                db?.shareCalendarScheduleDao()?.updateShareSchedule(selectedScheduleEdit)
                finish()
            }
        }
    }
}