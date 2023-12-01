package com.example.kotlincalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Gallery
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.adapters.ViewGroupBindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.kotlincalendar.Calendar.CalendarMain
import com.example.kotlincalendar.FriendList.Frd_management
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.Dao.UserDao
import com.example.kotlincalendar.databinding.ActivityCalendarBinding
import com.example.kotlincalendar.databinding.ActivityMyPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.util.jar.Attributes.Name

class my_page : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding
    private lateinit var drawerLayout: DrawerLayout
    private val GALLERY = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val menuBtn = binding.menuBtnMypage

        var db = AppDatabase.getInstance(this)
        val getUserEmail = intent.getStringExtra("user_email")

        val userEmailView = binding.userEmail
        val userNameView = binding.userName
        val userPassView = binding.userPasswd
        val userPassReView = binding.userPasswdRe
        val userBirthView = binding.userBirth
        val userPhoneView = binding.userPhone
        val userProfileView = binding.userProfile

        //회원 정보 데이터 => editText
        CoroutineScope(Dispatchers.IO).launch {
            val userDataDB = db?.userDao()!!.getUserId(getUserEmail)
            withContext(Dispatchers.Main) {
                val userDataList = userDataDB[0]
                val nameText = userDataList.Name.toString()
                val emailText = userDataList.Email.toString()
                val passText = userDataList.Password.toString()
                val passReText = userDataList.Password.toString()
                val birthText = userDataList.brith_date.toString()
                val phoneText = userDataList.PhoneNum.toString()
                val userProfileData = userDataList.Profile_img

                userNameView.setText(nameText)
                userEmailView.setText(emailText)
                userPassView.setText(passText)
                userPassReView.setText(passReText)
                userBirthView.setText(birthText)
                userPhoneView.setText(phoneText)

                Glide.with(this@my_page)
                    .load(userProfileData)
                    .into(userProfileView)
            }
        }

        userProfileView.setOnClickListener {
           //이미지를 클릭하면 갤러리로 이동
        }

        //햄버거 메뉴
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = binding.navigationView
        navigationView.inflateHeaderView(R.layout.navigation_header) // 헤더 레이아웃 설정

        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_name_header)
        val statusTextView = headerView.findViewById<TextView>(R.id.statusText)
        val userProfileImg = headerView.findViewById<ImageView>(R.id.userProfile)

        CoroutineScope(Dispatchers.IO).launch {
            val userName = db?.userDao()?.getUserId(getUserEmail)
            if (userName != null && userName.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    val userDataList = userName[0]
                    val userNameData = userDataList.Name
                    val userStatusData = userDataList.SubTitle
                    val userProfileData = userDataList.Profile_img

                    userNameTextView.text = userNameData
                    statusTextView.text = userStatusData
                    Glide.with(this@my_page)
                        .load(userProfileData)
                        .into(userProfileImg)

                }
            }
        }

        //로그아웃 기능
        val logoutBtn = headerView.findViewById<Button>(R.id.logout_Btn)
        logoutBtn.setOnClickListener {
            Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        menuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.my_Calendar -> {
                    val intent = Intent(this, CalendarMain::class.java)
                    intent.putExtra("user_email", getUserEmail)
                    startActivity(intent)
                    true
                }
                R.id.menu_share_Calendar -> {
                    true
                }
                R.id.menu_friend -> {
                    val intent = Intent(this, Frd_management::class.java)
                    intent.putExtra("user_email", getUserEmail)
                    startActivity(intent)
                    true
                }
                R.id.menu_userChange -> {
                    val intent = Intent(this, my_page::class.java)
                    intent.putExtra("user_email", getUserEmail)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


    }
}