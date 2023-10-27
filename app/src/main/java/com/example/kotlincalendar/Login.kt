package com.example.kotlincalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private var mBinding: ActivityLoginBinding? = null
private val binding get() = mBinding!!

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var db = AppDatabase.getInstance(this)

        val Login_btn=findViewById<Button>(R.id.login_btn)
        val Sing_Up_Btn=findViewById<TextView>(R.id.sing_up_btn_Login)
        val Current_User=findViewById<EditText>(R.id.Email_Login)
        val Passwd_User=findViewById<EditText>(R.id.PassWd_Login)

        //VAL cURRENT_


        Login_btn.setOnClickListener{
            val Current_User_:String=Current_User.getText().toString()
            val Passwd_User_:String=Passwd_User.getText().toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = db!!.userDao().get_Login(Current_User_, Passwd_User_)
                withContext(Dispatchers.Main) {

                    if (user != null) {
                        val intent = Intent(this@Login, MainPage::class.java)
                        intent.putExtra("user_email",Current_User_)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@Login,"이메일 또는 비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        Sing_Up_Btn.setOnClickListener{
            val intent=Intent(this,Sing_Up::class.java)
            startActivity(intent)
        }
    }
}