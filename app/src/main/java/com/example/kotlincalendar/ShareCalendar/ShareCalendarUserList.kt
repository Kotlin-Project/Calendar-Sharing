package com.example.kotlincalendar.ShareCalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kotlincalendar.Entity.User
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarUserListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShareCalendarUserList : AppCompatActivity() {
    private var mBinding:ActivityShareCalendarUserListBinding?=null
    private val binding get() = mBinding!!
    private var db= AppDatabase.getInstance(this)
    private var shareCalendarIdMemberList:String=""
    private var shareCalendarMasterUser:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityShareCalendarUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shareCalendarIdMemberList=intent.getStringExtra("shareCalendarId")!!
        shareCalendarMasterUser=intent.getStringExtra("userEmail")!!

        masterInitializeViews()
    }
    private fun masterInitializeViews(){
        GlobalScope.launch(Dispatchers.IO) {
            //val getMasterUserInformation=db!!.shareCalendarUserDao().getMasterUsersInformation(shareCalendarIdMemberList)
            val getMasterUserInformation=db!!.userDao().getMasterUserInfo(shareCalendarIdMemberList)
            val getMemberUserInformation=db!!.userDao().getMemberUserInfo(shareCalendarIdMemberList)
            withContext(Dispatchers.Main){
                val masterUserInfo=getMasterUserInformation
                val memberUserInfo=getMemberUserInformation
                memberInitializeViews(memberUserInfo)
                binding.masterUserName.text=masterUserInfo.Name
                Glide.with(this@ShareCalendarUserList)
                    .load(masterUserInfo.Profile_img) // 이미지 URL 또는 경로
                    .into(binding.masterImage) // ImageView
            }
        }
    }
    private fun memberInitializeViews(memberUserInfo:List<User>){
        Log.d("ShareCalendarScheduleAdd123123123", "123123123selectedDate: $memberUserInfo")
        val adapter = ShareCalendarUserListAdpater(memberUserInfo)
        binding.shareCalendarMemberList.adapter = adapter
        binding.shareCalendarMemberList.layoutManager = LinearLayoutManager(this)

    }
}