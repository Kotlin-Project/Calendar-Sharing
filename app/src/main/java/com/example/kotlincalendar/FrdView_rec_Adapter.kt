package com.example.kotlincalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.Entity.FriendAdd
import com.example.kotlincalendar.Entity.FriendList
import com.example.kotlincalendar.databinding.ListFriendRecItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FrdView_rec_Adapter(
    val context: Context,
    val frdRequests:List<FriendAdd>
    ) : BaseAdapter() {
    //private var mBinding: ListFriendRecItemBinding? = null
    //private val binding get() = mBinding!!
    var db = AppDatabase.getInstance(context)
    override fun getItem(position: Int): Any {
        return frdRequests[position]
    }
    override fun getCount(): Int {
        return frdRequests.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, covertView: View?, parent: ViewGroup?): View {

        val binding = ListFriendRecItemBinding.inflate(LayoutInflater.from(context))

        val icon = binding.frdImage
        val name = binding.textName
        val title = binding.textTitle
        val dis_btn=binding.disBtn
        val acc_btn=binding.accBtn

        val senderEmail=frdRequests[position].Sender_ID
        val receiverEmail=frdRequests[position].Receiver_ID
        CoroutineScope(Dispatchers.IO).launch {
            val senderUser = db?.userDao()?.getUserByEmail(senderEmail)
            withContext(Dispatchers.Main) {
                if (senderUser != null) {
                    icon.setImageResource(senderUser.Profile_img)
                    name.text = senderUser.Name
                    title.text = senderUser.SubTitle
                }
            }
        }
        acc_btn.setOnClickListener(){
            val friendlistAdd= FriendList(0, User1=senderEmail, User2=receiverEmail)
            CoroutineScope(Dispatchers.IO).launch {
                db?.friendListDao()?.insertFriend(friendlistAdd)
                db?.friendAddDao()?.deleteRequest(frdRequests[position])
                withContext((Dispatchers.Main)){
                    notifyDataSetChanged()
                    Toast.makeText(context, "신청을 수락했습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dis_btn.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch{
                db?.friendAddDao()?.deleteRequest(frdRequests[position])
                withContext(Dispatchers.Main){
                    notifyDataSetChanged()
                    Toast.makeText(context,"신청을 거절했습니다",Toast.LENGTH_SHORT).show()
                }
            }

        }
        return binding!!.root
    }
}