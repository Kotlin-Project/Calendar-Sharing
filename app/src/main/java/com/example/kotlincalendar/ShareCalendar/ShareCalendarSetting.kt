package com.example.kotlincalendar.ShareCalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kotlincalendar.Calendar.CalendarMain
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarSettingBinding
import com.example.kotlincalendar.databinding.ActivityShareScheduleEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class ShareCalendarSetting : AppCompatActivity() {
    private var mBinding: ActivityShareCalendarSettingBinding? = null
    private val binding get() = mBinding!!
    private var db=AppDatabase.getInstance(this@ShareCalendarSetting)
    private var shareCalendarId:String=""
    private var userEmail:String=""
    private var masterUser:Boolean=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityShareCalendarSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shareCalendarId = intent.getStringExtra("shareCalendarId")!!
        userEmail = intent.getStringExtra("userEmail")!!

        settingInitializeViews()
        GlobalScope.launch(Dispatchers.IO) {
            masterUser = db!!.shareCalendarUserDao().getMasterUserEmail(shareCalendarId, userEmail)
            withContext(Dispatchers.Main) {
                binding.settingEditBtn.setOnClickListener {
                    if (masterUser == true) {
                        editClick()
                    } else
                        Toast.makeText(this@ShareCalendarSetting, "권한이 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.settingDeleteBtn.setOnClickListener {
            deleteClick()
        }
    }

    private fun deleteBtnText(){
        if(masterUser==false)
            binding.btnText.setText("나가기")
    }

    private fun settingInitializeViews() {
        //db = AppDatabase.getInstance(this@ShareCalendarSetting)
        GlobalScope.launch(Dispatchers.IO) {
            val shareCalendarSetting = db!!.shareCalendarDao().getShareCalendar(shareCalendarId)
            withContext(Dispatchers.Main) {
                val imageResource = shareCalendarSetting.shareCalendarImage
                val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.getDefault())
                val formattedDate = dateFormat.format(shareCalendarSetting.createdAt)
                binding.settingTitleView.text = shareCalendarSetting.shareCalendarTitle
                binding.settingCategory.text = shareCalendarSetting.shareCalendarCategory
                binding.shareCalendarSubTitleView.text = shareCalendarSetting.shareCalendarSubTitle
                binding.shareCalendarImage.setImageResource(imageResource)
                binding.shareCalendarInvitationCode.text = shareCalendarSetting.shareCalendarId
                binding.shareCalendarCreationTime.text = formattedDate
                deleteBtnText()
            }
        }
    }
    private fun editClick(){
        val intent = Intent(this, ShareCalendarEdit::class.java)
        intent.putExtra("shareCalendarId", shareCalendarId)
        intent.putExtra("userEmail",userEmail)
        startActivity(intent)
    }
    private fun deleteClick(){
        if(masterUser==true)
            masterDeleteClick()
        else
            userDeleteClick()
    }
    private fun masterDeleteClick(){
        // AlertDialogBuilder 생성
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setMessage("이 캘린더를 삭제하시겠습니까?")
            //다이얼로그 외부를 터치 시 닫히지 않음
            .setCancelable(false)
            .setPositiveButton("예") { dialog, _ ->
                GlobalScope.launch(Dispatchers.IO) {
                    val shareCalendarDelete = db!!.shareCalendarDao().getShareCalendar(shareCalendarId)
                    db!!.shareCalendarDao().deleteShareCalendar(shareCalendarDelete)
                    db!!.shareCalendarUserDao().deleteShareCalendarUsersByCalendarId(shareCalendarId)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ShareCalendarSetting, "캘린더 삭제", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                dialog.dismiss() // 다이얼로그 닫기
            }
            // '아니요' 버튼 설정
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss() // 다이얼로그 닫기
            }

        // AlertDialog 생성 및 표시
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun userDeleteClick(){
        // AlertDialogBuilder 생성
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setMessage("이 캘린더에서 나가겠습니까?\n (내가 남긴 스케줄은 사라지지 않습니다)")
            //다이얼로그 외부를 터치 시 닫히지 않음
            .setCancelable(false)
            .setPositiveButton("예") { dialog, _ ->
                GlobalScope.launch(Dispatchers.IO) {
                    db!!.shareCalendarUserDao().secessionUserGetCalendarId(shareCalendarId, userEmail)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ShareCalendarSetting, "나가기 완료", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                dialog.dismiss() // 다이얼로그 닫기
            }
            // '아니요' 버튼 설정
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss() // 다이얼로그 닫기
            }

        // AlertDialog 생성 및 표시
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}