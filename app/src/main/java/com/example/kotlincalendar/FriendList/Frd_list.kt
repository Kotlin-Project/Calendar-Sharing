package com.example.kotlincalendar.FriendList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.FragmentFrdListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Frd_list : Fragment() {
    private var mBinding: FragmentFrdListBinding? = null
    private val binding get() = mBinding!!
    var db: AppDatabase? = null
    companion object {
        private const val USER_EMAIL_KEY = "user_email"
        fun newInstance(userEmail: String): Frd_list {
            val fragment = Frd_list()
            val args = Bundle()
            args.putString(USER_EMAIL_KEY, userEmail)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFrdListBinding.inflate(inflater, container, false)
        val view = binding.root
        val userEmail = arguments?.getString(USER_EMAIL_KEY).toString()
        val recyclerView=binding.listFrdView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        db = AppDatabase.getInstance(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val friendsList_ = db?.friendListDao()!!.getFriends(userEmail)
            withContext(Dispatchers.Main) {
                val friendListAdapter = Frd_list_Adapter(requireContext(),friendsList_, userEmail)
                recyclerView.adapter = friendListAdapter
                friendListAdapter.notifyDataSetChanged()
            }
        }
        return view
    }
}