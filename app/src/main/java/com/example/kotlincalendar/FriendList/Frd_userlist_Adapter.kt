package com.example.kotlincalendar.FriendList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.database.User
import com.example.kotlincalendar.databinding.ListFriendApplyItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Frd_userlist_Adapter (
    val context: Context,
    val friendUserList: List<User>,
    val userEmail: String) : RecyclerView.Adapter<Frd_userlist_Adapter.FrdViewHolder>() {
    lateinit var db: AppDatabase

    init {
        db = AppDatabase.getInstance(context)!!
    }

    //레이아웃 연결
    inner class FrdViewHolder(private val binding: ListFriendApplyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item_icon = binding.frdImage
        val item_name_=binding.textName
        val item_title = binding.textTitle
        val item_acc_btn = binding.accBtn

        //ViewHolder에 User에 있는 데이터 넣기
        fun bind(frdUser: User){
            item_icon.setImageResource(frdUser.Profile_img)
            item_name_.text = frdUser.Name
            item_title.text = frdUser.SubTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Frd_userlist_Adapter.FrdViewHolder {
        val binding = ListFriendApplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FrdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Frd_userlist_Adapter.FrdViewHolder, position: Int) {
        val userList = friendUserList[position]
        userList?.let{ holder.bind(it)}

/*        CoroutineScope(Dispatchers.IO).launch {
            val areFriends = db?.userDao()?.areFriends(userEmail, frd.Email)
            val isRequestPending = db?.userDao()?.isRequestPending(userEmail, frd.Email)
            if (areFriends == null && isRequestPending == null && userEmail!=frd.Email) {
                val friendRequest = frdadd_db(0, Sender_ID = senderEmail, Receiver_ID = receiverEmail)
                db?.frdaddDbDao()?.insertRequest(friendRequest)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "신청완료", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "이미 친구이거나 신청 목록을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }*/
    }


    //항목 수 반환
    override fun getItemCount(): Int {
        return friendUserList.size
    }
}