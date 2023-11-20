package com.example.kotlincalendar.FriendList

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class Frd_managemnet_Adapter(fragmentActivity: FragmentActivity, var UserEmail: String) :
    FragmentStateAdapter(fragmentActivity) {

    // 프래그먼트 수
    companion object {
        private const val NUM_PAGES = 3
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Frd_list.newInstance(UserEmail)
            1 -> Frd_acclist.newInstance(UserEmail)
            2 -> Frd_userlist.newInstance(UserEmail)
            else -> Frd_list()
        }
    }
}