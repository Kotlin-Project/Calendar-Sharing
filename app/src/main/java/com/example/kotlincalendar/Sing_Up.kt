package com.example.kotlincalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlincalendar.Entity.User
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivitySingUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private var mBinding: ActivitySingUpBinding? = null
private val binding get() = mBinding!!

class Sing_Up : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySingUpBinding.inflate(layoutInflater);
        setContentView(binding.root)

        var db = AppDatabase.getInstance(this)

        var Sing_Up_btn=binding.singUpSu

        var Email=binding.EmailSu
        val Password=binding.PassWdSu
        var Name=binding.NameSu
        var PhoneNum=binding.PhoneSu
        val brith_date=binding.BirthDateSu
        val Profile_img: Int = R.drawable.test
        val SubTitle: String = " "

        Sing_Up_btn.setOnClickListener() {

            val Email_: String = Email.getText().toString()
            val Password_:String=Password.getText().toString()
            val Name_: String = Name.getText().toString()
            val PhoneNum_: String = PhoneNum.getText().toString()
            val brith_date_: String = brith_date.getText().toString()

            val newUser = User(Email_, Password_,Name_, PhoneNum_, brith_date_, Profile_img, SubTitle)

            GlobalScope.launch(Dispatchers.IO) {
                db!!.userDao().insert(newUser)
            }
            Toast.makeText(this@Sing_Up,"가입완료",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}