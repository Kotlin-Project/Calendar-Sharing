package com.example.kotlincalendar.FriendList

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class Frd_managemnet_Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val NUM_PAGES = 2 // 프래그먼트 수
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Frd_list()
            1 -> Frd_userlist()
            else -> Frd_list()
        }
    }
}