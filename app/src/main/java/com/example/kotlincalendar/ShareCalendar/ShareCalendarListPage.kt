package com.example.kotlincalendar.ShareCalendar

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kotlincalendar.Calendar.CalendarMain
import com.example.kotlincalendar.Calendar.Calendar_Schedule_Add
import com.example.kotlincalendar.FriendList.Frd_management
import com.example.kotlincalendar.Login
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarListPageBinding
import com.example.kotlincalendar.my_page
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShareCalendarListPage : AppCompatActivity() {
    private var mBinding: ActivityShareCalendarListPageBinding? = null
    private val binding get() = mBinding!!
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var shareCalendarListAdapter:ShareCalendarListAdapter

    //private var userEmail:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityShareCalendarListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuBtn = binding.menuBtnShare

        var db = AppDatabase.getInstance(this)
        val userEmail = intent.getStringExtra("user_email")!!
        binding.ShareCalendarRecyclerView.layoutManager = LinearLayoutManager(this)

        shareCalendarListAdapter(userEmail)
        binding.joinCalendarBtn.setOnClickListener {
            joinCalendarDialog(userEmail)
        }
        binding.shareCalendarAddBtn.setOnClickListener{
            shareCalendarCreatePage(userEmail)
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
            val userDataDB = db?.userDao()?.getUserId(userEmail)

            // Check if userDataDB is not null and contains at least one item
            if (userDataDB != null && userDataDB.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    val userDataList = userDataDB[0]
                    val userNameData = userDataList.Name
                    val userStatusData = userDataList.SubTitle
                    val userProfileData = userDataList.Profile_img

                    userNameTextView.text = userNameData
                    statusTextView.text = userStatusData

                    //Log.d("UserData","Profile_img: $userProfileData")

                    // Use the correct activity reference (replace YourCalendarMainActivity with the actual name of your activity)
                    Glide.with(this@ShareCalendarListPage)
                        .load(userProfileData)
                        .into(userProfileImg)
                }
            } else {
                // Handle the case where userDataDB is null or empty
                // For example, set default values or show an error message
            }
        }

        statusTextView.setOnClickListener {
            showCustomDialog(this)
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
                    intent.putExtra("user_email", userEmail)
                    startActivity(intent)
                    true
                }
                R.id.menu_share_Calendar -> {
                    val intent=Intent(this, ShareCalendarListPage::class.java)
                    intent.putExtra("user_email",userEmail)
                    startActivity(intent)
                    true
                }
                R.id.menu_friend -> {
                    val intent = Intent(this, Frd_management::class.java)
                    intent.putExtra("user_email",userEmail)
                    startActivity(intent)
                    true
                }
                R.id.menu_userChange -> {
                    val intent = Intent(this, my_page::class.java)
                    intent.putExtra("user_email", userEmail)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
    private fun shareCalendarCreatePage(userEmail:String){
        val intent = Intent(this, ShareCalendarCreate::class.java)
        intent.putExtra("user_email", userEmail)
        startActivity(intent)
    }
    private fun shareCalendarListAdapter(userEmail: String){
        var db= AppDatabase.getInstance(this)
        GlobalScope.launch(Dispatchers.IO) {
            val shareCalendarIdList = db!!.shareCalendarUserDao().getShareCalendarIdsForUser(userEmail)
            //Log.d("ShareCalendarCreate", "shareCalendarIdList--------ID: $shareCalendarIdList")
            val shareCalendarList = db.shareCalendarDao().getShareCalendarsByIds(shareCalendarIdList)
            //Log.d("ShareCalendarCreate", "shareCalendarIdList--------ID: $shareCalendarList")
            //Log.d("ShareCalendarCreate", "shareCalendarIdList--------ID: $userEmail")
            withContext(Dispatchers.Main) {
                shareCalendarListAdapter = ShareCalendarListAdapter(this@ShareCalendarListPage, shareCalendarList, userEmail)
                binding.ShareCalendarRecyclerView.adapter = shareCalendarListAdapter
            }
        }
    }

    private fun joinCalendarDialog(userEmail: String) {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_joincalendar)

        val codeText = dialog.findViewById<EditText>(R.id.codeText)
        val cancelBtn = dialog.findViewById<Button>(R.id.joinCancelBtn)
        val joinCalendarBtn = dialog.findViewById<Button>(R.id.joinConformBtn)

        joinCalendarBtn.setOnClickListener {
            val codeInsertText = codeText.text.toString()
            val intent = Intent(this, JoinCalendarInformation::class.java)
            intent.putExtra("codeInsertText", codeInsertText)
            intent.putExtra("userEmail",userEmail)
            startActivity(intent)
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        val userEmail = intent.getStringExtra("user_email")!!
        shareCalendarListAdapter(userEmail)
    }

    private fun showCustomDialog(context : Context) {
        val statusDialog = Dialog(context)
        statusDialog.setContentView(R.layout.dialog_status)
        val statusEditText =  statusDialog.findViewById<EditText>(R.id.statusEditText)
        val dialogBackBtn = statusDialog.findViewById<Button>(R.id.backBtn)
        val dialogChangeBtn = statusDialog.findViewById<Button>(R.id.conformBtn)
        var dialogDB = AppDatabase.getInstance(this)
        val statusUserEmail = intent.getStringExtra("user_email")
        CoroutineScope(Dispatchers.IO).launch {
            val userStatusDB = dialogDB?.userDao()!!.getUserId(statusUserEmail)
            withContext(Dispatchers.Main) {
                val userDB = userStatusDB[0]
                val statusDialog = userDB.SubTitle

                statusEditText.setText(statusDialog)
            }
        }

        dialogBackBtn.setOnClickListener {
            statusDialog.dismiss()
        }

        dialogChangeBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val userStatusDB = dialogDB?.userDao()!!.getUserId(statusUserEmail)
                withContext(Dispatchers.Main) {
                    val userDB = userStatusDB[0]
                    val newStatus = statusEditText.text.toString()
                    val updateResult = updateUserStatus(userDB.Email, newStatus)

                    if(updateResult) {
                        statusDialog.dismiss()
                        //새로고침..?
                        finish()
                        overridePendingTransition(0, 0)
                        val intent = getIntent()
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    } else {
                        showToast("변경 실패")
                    }
                }
            }
        }
        statusDialog.show()
    }
    private fun updateUserStatus(statusUserEmail: String, newStatus: String): Boolean{
        return try {
            var dialogDB = AppDatabase.getInstance(this)
            CoroutineScope(Dispatchers.IO).launch {
                dialogDB?.userDao()?.updateStatus(statusUserEmail, newStatus)
            }
            true // 변경 성공
        } catch (e: Exception) {
            false // 변경 실패
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}