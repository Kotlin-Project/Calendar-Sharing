package com.example.kotlincalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.kotlincalendar.Calendar.CalendarMain
import com.example.kotlincalendar.databinding.ActivityMainPageBinding

private var mBinding: ActivityMainPageBinding? = null
private val binding get() = mBinding!!

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val userEmail = intent.getStringExtra("user_email")
        if (userEmail != null) {
            Toast.makeText(this@MainPage,"로그인 성공", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this@MainPage,"로그인에 문제가 발생하였습니다 재시도 해주세요", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainPage, Login::class.java) // LoginActivity는 로그인 화면 액티비티의 이름에 해당
            startActivity(intent)
            finish()
        }
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainPageBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val my_btn = findViewById<Button>(R.id.My_btn)
        val frd_btn = findViewById<Button>(R.id.frd_btn)
        val share_btn = findViewById<Button>(R.id.share_btn)
        val friend_btn = findViewById<Button>(R.id.friend_)


        my_btn.setOnClickListener{
            val intent = Intent(this, CalendarMain::class.java)
            intent.putExtra("user_email",userEmail)
            startActivity(intent)
        }
        friend_btn.setOnClickListener{
            val intent = Intent(this, Friend_list::class.java)
            intent.putExtra("user_email",userEmail)
            startActivity(intent)
        }
    }
}