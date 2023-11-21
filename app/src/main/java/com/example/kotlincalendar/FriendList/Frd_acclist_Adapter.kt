package com.example.kotlincalendar.FriendList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.Entity.FriendAdd
import com.example.kotlincalendar.Entity.User
import com.example.kotlincalendar.databinding.ListFriendRecItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Frd_acclist_Adapter(val context: Context,
                          val frdRequests:List<FriendAdd>,
                          val userEmail: String) : RecyclerView.Adapter<Frd_acclist_Adapter.FrdViewHolder>() {
    lateinit var db: AppDatabase

    init {
        db = AppDatabase.getInstance(context)!!
    }

    inner class FrdViewHolder(private val binding: ListFriendRecItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item_icon = binding.frdImage
        val item_name_=binding.textName
        val item_title = binding.textTitle
        val item_Acc_btn = binding.accBtn
        val item_del_btn=binding.disBtn

        //ViewHolder
        fun bind(frdUser: User){
            item_icon.setImageResource(frdUser.Profile_img)
            item_name_.text = frdUser.Name
            item_title.text = frdUser.SubTitle
        }
    }

    // ViewHolder를 생성하고 레이아웃 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrdViewHolder {
        val binding = ListFriendRecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FrdViewHolder(binding)
    }
    //데이터 바인딩
    override fun onBindViewHolder(holder: Frd_acclist_Adapter.FrdViewHolder, position: Int) {
        val senderEmail=frdRequests[position].Sender_ID
        val receiverEmail=frdRequests[position].Receiver_ID

        CoroutineScope(Dispatchers.IO).launch {
            val senderUser = db?.userDao()?.getUserByEmail(senderEmail)
            withContext(Dispatchers.Main) {
                senderUser?.let{ holder.bind(it)}
                //if (senderUser != null) <-이거랑 같음
            }
        }
    }
    //항목 수 반환
    override fun getItemCount(): Int {
        return frdRequests.size
    }
}