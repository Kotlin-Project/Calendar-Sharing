package com.example.kotlincalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.kotlincalendar.Entity.User
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ListFriendApplyItemBinding


class FrdView_apply_Adapter(
    val context: Context,
    val userList:List<User>,
    val userEmail:String
    ) : BaseAdapter() {
    private var mBinding: ListFriendApplyItemBinding? = null
    private val binding get() = mBinding!!
    var db = AppDatabase.getInstance(context)

    private var filteredUserList = emptyList<User>()

    override fun getItem(position: Int): Any {
        return userList[position]
    }
    override fun getCount(): Int {
        return userList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, covertView: View?, parent: ViewGroup?): View {

        mBinding = ListFriendApplyItemBinding.inflate(LayoutInflater.from(context))

        val icon = binding.frdImage
        val name = binding.textName
        val title = binding.textTitle
        val acc_btn=binding.accBtn

        val frd = userList[position]
        icon.setImageResource(frd.Profile_img)
        name.text = frd.Name
        title.text = frd.SubTitle



/*        acc_btn.setOnClickListener {
            val senderEmail = userEmail
            val receiverEmail = frd.Email

            CoroutineScope(Dispatchers.IO).launch {
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
            }
        }*/
        return mBinding!!.root
    }
}