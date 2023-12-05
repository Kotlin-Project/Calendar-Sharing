package com.example.kotlincalendar.ShareCalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincalendar.Calendar.Calendar_Schedule_Add
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarListPageBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShareCalendarListPage : AppCompatActivity() {
    private var mBinding: ActivityShareCalendarListPageBinding? = null
    private val binding get() = mBinding!!
    private lateinit var shareCalendarListAdapter:ShareCalendarListAdapter

    //private var userEmail:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityShareCalendarListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userEmail = intent.getStringExtra("user_email")!!
        binding.ShareCalendarRecyclerView.layoutManager = LinearLayoutManager(this)

        shareCalendarListAdapter(userEmail)
        binding.shareCalendarAddBtn.setOnClickListener{
            shareCalendarCreatePage(userEmail)
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
}