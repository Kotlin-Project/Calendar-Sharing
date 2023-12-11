package com.example.kotlincalendar.ShareCalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kotlincalendar.Entity.ShareCalendarUser
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityJoinCalendarInformationBinding
import com.example.kotlincalendar.databinding.ActivityShareCalendarCreateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class JoinCalendarInformation : AppCompatActivity() {
    private var mBinding: ActivityJoinCalendarInformationBinding? = null
    private val binding get() = mBinding!!
    private var db= AppDatabase.getInstance(this@JoinCalendarInformation)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityJoinCalendarInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shareCalendarId = intent.getStringExtra("codeInsertText")!!
        val userEmail = intent.getStringExtra("userEmail")!!
        //Log.d("ShareCalendarCreate", "shareCalendarIdList--------ID: $shareCalendarId")
        settingInitializeViews(shareCalendarId)
        binding.joinBtn.setOnClickListener {
            joinCalendarBtnClick(shareCalendarId,userEmail)
        }
        binding.cancelBtn.setOnClickListener {
            cancelBtnClick()
        }
    }
    private fun settingInitializeViews(shareCalendarId: String) {
        //db = AppDatabase.getInstance(this@ShareCalendarSetting)
        GlobalScope.launch(Dispatchers.IO) {
            val shareCalendarSetting = db!!.shareCalendarDao().getShareCalendar(shareCalendarId)
            //Log.d("ShareCalendarCreate", "abcdefshareCalendarIdList--------ID: $shareCalendarSetting")

            withContext(Dispatchers.Main) {
                if(shareCalendarSetting==null){
                    Toast.makeText(this@JoinCalendarInformation, "해당 캘린더가 없습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    val imageResource = shareCalendarSetting.shareCalendarImage
                    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.getDefault())
                    val formattedDate = dateFormat.format(shareCalendarSetting.createdAt)
                    binding.settingTitleView.text = shareCalendarSetting.shareCalendarTitle
                    binding.settingCategory.text = shareCalendarSetting.shareCalendarCategory
                    binding.shareCalendarSubTitleView.text = shareCalendarSetting.shareCalendarSubTitle
                    binding.shareCalendarImage.setImageResource(imageResource)
                    binding.shareCalendarCreationTime.text = formattedDate
                }
            }
        }
    }
    private fun cancelBtnClick(){
        finish()
    }
    private fun joinCalendarBtnClick(shareCalendarId:String,userEmail:String){
        val shareCalendarUser = ShareCalendarUser(
            shareCalendarId =shareCalendarId,
            userEmail = userEmail,
            masterUser = false,
            canManagerEvent = false,
            canEvent = false
        )
        GlobalScope.launch(Dispatchers.IO){
            db?.shareCalendarUserDao()?.joinShareCalendar(shareCalendarUser)
            withContext(Dispatchers.Main){
                Toast.makeText(this@JoinCalendarInformation, "캘린더 생성 완료", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}