package com.example.kotlincalendar.FriendList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.database.User
import com.example.kotlincalendar.database.frdlist_db
import com.example.kotlincalendar.databinding.ListFriendItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




class Frd_list_Adapter(
    val context: Context,
    val friendList: List<frdlist_db>,
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

        //ViewHolder에 User에 있는 데이터 넣기
        fun bind(frdUser: User){
            item_icon.setImageResource(frdUser.Profile_img)
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