package com.example.kotlincalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.Entity.FriendList
import com.example.kotlincalendar.databinding.ListFriendItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FrdViewAdapter(
    val context: Context,
    val FriendsList:List<FriendList>,
    val userEmail:String
    ): BaseAdapter() {
    private var mBinding: ListFriendItemBinding? = null
    private val binding get() = mBinding!!
    var db = AppDatabase.getInstance(context)
    override fun getItem(position: Int): Any {
        return FriendsList[position]
    }
    override fun getCount(): Int {
        return FriendsList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, covertView: View?, parent: ViewGroup?): View {

        mBinding = ListFriendItemBinding.inflate(LayoutInflater.from(context))

        val icon = binding.frdImage
        val name = binding.textName
        val title = binding.textTitle
        val delete_btn=binding.accBtn


        val frdList=FriendsList[position]
        val friendEmail=if(frdList.User1==userEmail) frdList.User2 else frdList.User1

        CoroutineScope(Dispatchers.IO).launch {
            val frdUser=db?.userDao()?.getUserByEmail(friendEmail)
            withContext(Dispatchers.Main){
                if(frdUser!=null) {
                    icon.setImageResource(frdUser.Profile_img)
                    name.text=frdUser.Name
                    title.text=frdUser.SubTitle
                }
            }
        }

        delete_btn.setOnClickListener {
            Toast.makeText(context,"삭제", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch{
                db?.friendListDao()?.deleteFriend(FriendsList[position])
                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                    Toast.makeText(context,"삭제했습니다",Toast.LENGTH_SHORT).show()
                }
            }

        }
        return mBinding!!.root
    }


}