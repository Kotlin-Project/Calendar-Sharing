package com.example.kotlincalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.Entity.FriendAdd
import com.example.kotlincalendar.Entity.User
import com.example.kotlincalendar.databinding.ActivityFriendListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Friend_list : AppCompatActivity() {
    private var mBinding: ActivityFriendListBinding? = null
    private val binding get() = mBinding!!
    var db: AppDatabase? = null
    var User_List = arrayListOf<User>()
    var frdadd_List = arrayListOf<FriendAdd>()
    //var FrdAdd_rec_List=arrayListOf<frdadd_db>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFriendListBinding.inflate(layoutInflater);
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)
//--------------------------------------(나중에 Friend_list_ViewModel 로 옮기기)
        CoroutineScope(Dispatchers.IO).launch {
            val savedContacts = db!!.userDao().Select_frd()
            if (savedContacts.isNotEmpty()) {
                User_List.addAll(savedContacts)
            }
        }
//--------------------------------------
        val userEmail = intent.getStringExtra("user_email")
        val userEmail_: String = userEmail.toString()
        val frd_btn = binding.friendBtn
        val enroll_btn = binding.enrollBtn
        val search_btn = binding.searchBtn

//------------------------------------------------------
        frd_btn.setOnClickListener {
            frd_btn.isSelected = frd_btn.isSelected != true
            CoroutineScope(Dispatchers.IO).launch {
                val friendsList_ = db?.friendListDao()!!.getFriends(userEmail_)
                withContext(Dispatchers.Main) {
                    val Adapter = FrdViewAdapter(this@Friend_list, friendsList_, userEmail_)
                    binding.frdListView.adapter = Adapter
                    Adapter.notifyDataSetChanged()
                }
            }
        }
//------------------------------------------------------
        enroll_btn.setOnClickListener {
            enroll_btn.isSelected = enroll_btn.isSelected != true
            CoroutineScope(Dispatchers.IO).launch {
                val friendRequests = db?.friendAddDao()!!.getRequests(userEmail_)

                withContext(Dispatchers.Main) {
                    val Adapter = FrdView_rec_Adapter(this@Friend_list, friendRequests)
                    binding.frdListView.adapter = Adapter
                    Adapter.notifyDataSetChanged()
                }
            }
        }
//------------------------------------------------------
        search_btn.setOnClickListener {
            search_btn.isSelected = search_btn.isSelected != true
            CoroutineScope(Dispatchers.IO).launch {
                val friendsList = db?.friendListDao()?.getFriends(userEmail_)
                val sentRequests = db?.friendAddDao()?.getSentRequests(userEmail_)
                val receivedRequests = db?.friendAddDao()?.getRequests(userEmail_)
                val allUsers = db?.userDao()?.Select_frd()

                val filterUsers = allUsers!!.filter { user ->
                    !friendsList?.any { it.User1 == user.Email || it.User2 == user.Email }!! &&
                            !sentRequests?.any { it.Receiver_ID == user.Email }!! &&
                            !receivedRequests?.any { it.Sender_ID == user.Email }!! &&
                            user.Email!=userEmail_
                }
                withContext(Dispatchers.Main) {
                    val Adapter = FrdView_apply_Adapter(this@Friend_list, filterUsers, userEmail_)
                    binding.frdListView.adapter = Adapter
                    Adapter.notifyDataSetChanged()
                }
            }
        }
    }


/*    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }*/
}
