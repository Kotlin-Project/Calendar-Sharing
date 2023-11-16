package com.example.kotlincalendar.FriendList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlincalendar.databinding.ActivityFrdManagementBinding

private var mBinding: ActivityFrdManagementBinding? = null
private val binding get() = mBinding!!

class Frd_management : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: Frd_managemnet_Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityFrdManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.FrdViewPager2
        adapter = Frd_managemnet_Adapter(this)

        viewPager.adapter = adapter
    }
}