package com.example.kotlincalendar

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.kotlincalendar.Calendar.CalendarMain
import com.example.kotlincalendar.FriendList.Frd_management
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityMyPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val changeBtn = binding.changeBtn

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

        //정보 변경 버튼 클릭 시 정보 업데이트
        changeBtn.setOnClickListener {
            var nameEdit = binding.userName.getText().toString()
            var EmailEdit = binding.userEmail.getText().toString()
            var passEdit = binding.userPasswd.getText().toString()
            var birthEdit = binding.userBirth.getText().toString()
            var phoneEdit = binding.userPhone.getText().toString()
            //var profileEdit = "data:image/jpeg;base64,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"

            CoroutineScope(Dispatchers.IO).launch {
                val userName = db?.userDao()!!.getUserId(getUserEmail)
                if (userName != null && userName.isNotEmpty()) {
                    val userDataList = userName[0]

                    userDataList.Name = nameEdit
                    userDataList.Email = EmailEdit
                    userDataList.Password = passEdit
                    userDataList.PhoneNum = birthEdit
                    userDataList.brith_date = phoneEdit
                    //userDataList.Profile_img = profileEdit

                    db?.userDao()?.update(userDataList)
                    finish()
                }
            }
        }

        //이미지 클릭 시 갤러리 오픈 후 이미지 변경
        userProfileView.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                -> {
                    navigateGallery()
                }

                // 갤러리 접근 권한이 없는 경우 & 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                -> {
                    showPermissionContextPopup()
                }

                // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
                else -> requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }

        }
    }

    // 권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigateGallery()
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        // 가져올 컨텐츠들 중에서 Image 만을 가져온다.
        intent.type = "image/*"
        // 갤러리에서 이미지를 선택한 후, 프로필 이미지뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val userProfileView = binding.userProfile
        super.onActivityResult(requestCode, resultCode, data)

        // 예외처리
        if (resultCode != Activity.RESULT_OK)
            return

        when (requestCode) {
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위해서
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    // Convert URI to URL
                    val imageUrl = selectedImageUri.toString()

                    // Now you can use the imageUrl as needed
                    userProfileView.setImageURI(selectedImageUri)

                    // 여기에서 데이터베이스를 업데이트하도록 호출
                    updateProfileInDatabase(imageUrl)
                    // You can also use the imageUrl to pass or display elsewhere in your code
                    // For example, you might want to store it in a variable or upload it to a server
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 데이터베이스 업데이트를 위한 메서드
    private fun updateProfileInDatabase(newProfile: String?) {
        var db = AppDatabase.getInstance(this)
        CoroutineScope(Dispatchers.IO).launch {
            val userEmail = intent.getStringExtra("user_email")// 여기에서 사용자 이메일을 가져오는 코드 추가
            if (!userEmail.isNullOrBlank()) {
                db?.userDao()?.updateProfile(userEmail, newProfile ?: "")
            }
        }
    }


    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }
}