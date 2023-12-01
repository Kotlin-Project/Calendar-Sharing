package com.example.kotlincalendar.FriendList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.Entity.FriendList
import com.example.kotlincalendar.Entity.User
import com.example.kotlincalendar.FriendCalendar.FriendCalendarMain
import com.example.kotlincalendar.databinding.ListFriendItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




class Frd_list_Adapter(
    val context: Context,
    val friendList: List<FriendList>,
    val userEmail: String) : RecyclerView.Adapter<Frd_list_Adapter.FrdViewHolder>() {
    lateinit var db: AppDatabase

    init {
        db = AppDatabase.getInstance(context)!!
    }

    //레이아웃 연결
    inner class FrdViewHolder(private val binding: ListFriendItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item_icon = binding.frdImage
        val item_name_=binding.textName
        val item_title = binding.textTitle
        val item_delete_btn = binding.accBtn

        init {
            itemView.setOnClickListener {
                val currentFriend = friendList[absoluteAdapterPosition]
                val friendEmail = if (currentFriend.User1 == userEmail) currentFriend.User2 else currentFriend.User1

                val intent = Intent(context, FriendCalendarMain::class.java)
                intent.putExtra("friend_email", friendEmail)
                intent.putExtra("user_email",userEmail)
                context.startActivity(intent)
            }

            //친구 삭제
            item_delete_btn.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentFriend = friendList[position]
                    CoroutineScope(Dispatchers.IO).launch {
                        db?.friendListDao()?.deleteFriend(currentFriend)
                        withContext(Dispatchers.Main) {
                            notifyDataSetChanged()
                            Toast.makeText(context, "삭제했습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        //ViewHolder에 User에 있는 데이터 넣기
        fun bind(frdUser: User){
            Glide.with(context)
                .load(frdUser.Profile_img)
                .into(item_icon)
            item_name_.text = frdUser.Name
            item_title.text = frdUser.SubTitle
        }
    }

    // ViewHolder를 생성하고 레이아웃 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrdViewHolder {
        val binding = ListFriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FrdViewHolder(binding)
    }

    //데이터 바인딩
    override fun onBindViewHolder(holder: FrdViewHolder, position: Int) {
        val currentFriend = friendList[position]
        val friendEmail = if (currentFriend.User1 == userEmail) currentFriend.User2 else currentFriend.User1
        CoroutineScope(Dispatchers.IO).launch {
            val frdUser = db?.userDao()?.getUserByEmail(friendEmail)
            withContext(Dispatchers.Main) {
                    frdUser?.let{ holder.bind(it)}
                //if (frdUser != null) <-이거랑 같음
            }
        }

    }

    //항목 수 반환
    override fun getItemCount(): Int {
        return friendList.size
    }
}