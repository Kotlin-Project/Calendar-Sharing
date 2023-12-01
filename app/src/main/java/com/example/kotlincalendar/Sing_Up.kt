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
        val Profile_img: String = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys/RD84QzQ5OjcBCgoKDQwNGg8PGjclHyU3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3N//AABEIAL0AyAMBIgACEQEDEQH/xAAaAAEAAwEBAQAAAAAAAAAAAAAAAgMFBAEH/8QALxABAAIBAgIIBAcBAAAAAAAAAAECAwQREjEUM0FSYXFyoSEyUbETIkKBkaLxY//EABQBAQAAAAAAAAAAAAAAAAAAAAD/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwD6cAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACdcWS/y0mf2BAXdFzdz3hC2LJT5qTH7AgAAAAAAAAAAAAAAAAAAAAsxYrZbbVjzn6I46TkvFa85amLHXHSK1/0FeLTY8fZxW+srgAABTl02PJ2cNvrDhy4rYrbWjyn6tRHLjrkpNbf4DJEslJx3mtucIgAAAAAAAAAAAAAAAA7dBj2rOSec/CHWr00bYKR4brAAAAAAAcmvx71jJHOPhLiampjfBePDdlgAAAAAAAAAAAAAAAA1cE74aemE3Pob8WLh7ay6AAAAAAAQzzthv6ZZTQ11+HFw9tpZ4AAAAAAAAAAAAAAAALdPl/CyRbs5S04mJiJid4ljujTaicX5bfGn2BoDyl63jesxMPQAACZiImZnaIeXvWkb2mIhwajUTl/LX4U+4IajL+Lkm3ZyhUAAAAAAAAAAAAAAAAAAc1lcOW3Klv4BGl7UneszE+C+usyR80RZDoubue8HRc3c94Bd07/n/ZC2syT8sRVDoubue8HRc3c94BXe9rzvaZmfFFd0XN3PeEbYctedLfwCsOQAAAAAAAAAAAAAC7T4LZp35VjnIK6Utknakby7MWjrHxyTv4Q6KUrjrw1jaEgRpStI2rWI8kgAAAAAABG9K3ja1Ynzc+XR1n44528JdQDJvS2OdrxtKLWvSuSvDaN4Z+owWwzvzrPKQUgAAAAAAAA9iN52jnILNPhnLfb9Mc5aVaxWIisbRCGDHGLHFe3tWAAAAAAAAAAAAAPLVi0TFo3iXoDM1GGcN9v0zylU1M+OMuOa9vYzJjadp5wDwAAAAAB0aKnFl4p5V+7naGhrth370g6AAAAAAAAAAAAAAAAGfracOXijlb7tBz66u+HfuyDPAAAAAAamnjbBTyZbVw9Tj9MAmAAAAAAAAAAAAAAAAr1Eb4L+SxDN1N/TIMoAAAAABq4epx+mGU1cPU4/TAJgAAAAAAAAAAAAAAAIZupv6ZTQzdTf0yDKAAAB/9k="
        val SubTitle: String = "한줄소개 데이터 테스트중"

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