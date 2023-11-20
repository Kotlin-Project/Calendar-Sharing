package com.example.kotlincalendar.FriendList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlincalendar.databinding.ActivityFrdManagementBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private var mBinding: ActivityFrdManagementBinding? = null
private val binding get() = mBinding!!

class Frd_management : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: Frd_managemnet_Adapter
    private lateinit var tabLayout:TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityFrdManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userEmail = intent.getStringExtra("user_email")
        val userEmail_: String = userEmail.toString()

        viewPager = binding.FrdViewPager2
        tabLayout=binding.FrdTab

        adapter = Frd_managemnet_Adapter(this,userEmail_)

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout,viewPager){tab, position->
            when(position){
                0 -> tab.text="친구 목록"
                1 -> tab.text="친구 관리"
                2 -> tab.text="유저 목록"
            }
        }.attach()
    }
}