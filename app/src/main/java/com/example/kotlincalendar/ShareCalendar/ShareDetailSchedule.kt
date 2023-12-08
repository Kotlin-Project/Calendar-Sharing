package com.example.kotlincalendar.ShareCalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kotlincalendar.Entity.ShareCalendarSchedule
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareDetailScheduleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShareDetailSchedule : AppCompatActivity() {
    private var mBinding: ActivityShareDetailScheduleBinding? = null
    private val binding get() = mBinding!!
    private var db: AppDatabase? = null
    private var shareScheduleId:Long=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityShareDetailScheduleBinding.inflate(layoutInflater);
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)
        shareScheduleId = intent.getLongExtra("scheduleId", -1)
        Log.d("asdasdasd", "asdasd--------ID: $shareScheduleId")


        GlobalScope.launch(Dispatchers.IO) {
            val deleteSchedule=db?.shareCalendarScheduleDao()!!.getScheduleItemById(shareScheduleId)
            //db?.shareCalendarScheduleDao()!!.deleteShareSchedule(deleteSchedule[0])
            withContext(Dispatchers.Main){
                val selectedSchedule=deleteSchedule[0]

                initializeViews(selectedSchedule)
                binding.editBtn.setOnClickListener {
                    shareScheduleEditBtnClick()
                }
                binding.deleteBtn.setOnClickListener{
                    deleteBtnClick(selectedSchedule)
                }
            }
        }

    }
    //디테일정보 삽입
    private fun initializeViews(selectedSchedule:ShareCalendarSchedule){
        //GlobalScope.launch(Dispatchers.IO) {
        //    val shareSchedule =db?.shareCalendarScheduleDao()!!.getScheduleItemById(shareScheduleId)
        //    withContext(Dispatchers.Main) {
                //val selectedSchedule = shareSchedule[0]
                binding.shareDetailTitle.text=selectedSchedule.shareScheduleTitle
                binding.shareWriteUser.text=selectedSchedule.userEmail
                binding.localDateDetail.text=selectedSchedule.shareScheduleLocalDate.toString()
                binding.starTimeDetail.text=selectedSchedule.shareScheduleStart.toString()
                binding.finishTimeDetail.text=selectedSchedule.shareScheduleEnd.toString()
                binding.memoTextDetail.text=selectedSchedule.shareScheduleMemo
        //    }
        //}
    }
    //삭제버튼
    private fun deleteBtnClick(selectedSchedule:ShareCalendarSchedule) {
            GlobalScope.launch(Dispatchers.IO) {
                //val deleteSchedule=db?.shareCalendarScheduleDao()!!.getScheduleItemById(shareScheduleId)
                db?.shareCalendarScheduleDao()!!.deleteShareSchedule(selectedSchedule)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ShareDetailSchedule, "삭제 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
    }
    //수정버튼
    private fun shareScheduleEditBtnClick(){
            val intent = Intent(this, ShareScheduleEdit::class.java)
            intent.putExtra("shareScheduleId", shareScheduleId)
            startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        refreshScheduleDetails(shareScheduleId)
    }
    //UI재적용
    private fun refreshScheduleDetails(shareScheduleId:Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val shareSchedule =db?.shareCalendarScheduleDao()!!.getScheduleItemById(shareScheduleId)
            withContext(Dispatchers.Main) {
                val selectedSchedule = shareSchedule[0]
                binding.shareDetailTitle.text=selectedSchedule.shareScheduleTitle
                binding.shareWriteUser.text=selectedSchedule.userEmail
                binding.localDateDetail.text=selectedSchedule.shareScheduleLocalDate.toString()
                binding.starTimeDetail.text=selectedSchedule.shareScheduleStart.toString()
                binding.finishTimeDetail.text=selectedSchedule.shareScheduleEnd.toString()
                binding.memoTextDetail.text=selectedSchedule.shareScheduleMemo
            }
        }
    }
}
/*        editBtn.setOnClickListener {
            val intent = Intent(this, CalendarScheduleEdit::class.java)
            intent.putExtra("scheduleItem", shareScheduleId)
            startActivity(intent)
        }*/