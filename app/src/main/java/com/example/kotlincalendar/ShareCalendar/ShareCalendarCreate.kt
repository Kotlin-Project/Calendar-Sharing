package com.example.kotlincalendar.ShareCalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kotlincalendar.CustomSpinnerAdapter
import com.example.kotlincalendar.Entity.ShareCalendar
import com.example.kotlincalendar.Entity.ShareCalendarUser
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarCreateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ShareCalendarCreate : AppCompatActivity() {
    private var mBinding: ActivityShareCalendarCreateBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityShareCalendarCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userEmail = intent.getStringExtra("user_email")

        setSpinner()
        binding.shareCalendarCreateBtn.setOnClickListener {
            val msaterEmail=userEmail!!
            createShareCalendar(msaterEmail)
        }
    }
    private fun setSpinner(){
        val categoryList = arrayOf("친구", "업무", "연인", "가족").toList()
        val adapter = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.shareCalendarCategory.adapter = adapter
    }
    private fun createShareCalendar(masterEmail:String){
        var db = AppDatabase.getInstance(this)

        val calendarImage= R.drawable.test
        val calendarTitle=binding.shareCalendarTitle.getText().toString()
        val calendarSubTitle=binding.shareCalendarSubTitle.getText().toString()
        val calendarCategory = binding.shareCalendarCategory.selectedItem.toString()
        val RandomId=UUID.randomUUID().toString()
        val createCalendar= ShareCalendar(
            shareCalendarId = RandomId,
            shareCalendarTitle = calendarTitle,
            shareCalendarSubTitle=calendarSubTitle,
            shareCalendarImage = calendarImage,
            shareCalendarCategory = calendarCategory)
        GlobalScope.launch(Dispatchers.IO) {
            val lastInsertedCalendarId = db?.shareCalendarDao()?.insertShareCalendar(createCalendar)
            lastInsertedCalendarId?.let{calendarId->
                Log.d("ShareCalendarCreate", "Last inserted calendar ID: $calendarId")
                val shareCalendarUser = ShareCalendarUser(
                    shareCalendarId =RandomId,
                    userEmail = masterEmail,
                    masterUser = true,
                    canManagerEvent = true,
                    canEvent = true
                )
                db?.shareCalendarUserDao()?.joinShareCalendar(shareCalendarUser)
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ShareCalendarCreate, "캘린더 생성 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}