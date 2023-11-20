package com.example.kotlincalendar.FriendList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.FragmentFrdAcclistBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Frd_acclist : Fragment() {
    private var mBinding: FragmentFrdAcclistBinding? = null
    private val binding get() = mBinding!!
    var db: AppDatabase? = null

    companion object {
        private const val USER_EMAIL_KEY = "user_email"
        fun newInstance(userEmail: String): Frd_acclist {
            val fragment = Frd_acclist()
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
        mBinding = FragmentFrdAcclistBinding.inflate(inflater, container, false)
        val view = binding.root
        val userEmail = arguments?.getString(USER_EMAIL_KEY).toString()
        val recyclerView=binding.acclistFrdView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        db = AppDatabase.getInstance(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val friendRequests = db?.frdaddDbDao()!!.getRequests(userEmail)
            withContext(Dispatchers.Main) {
                val friendAcclistAdapter = Frd_acclist_Adapter(requireContext(),friendRequests, userEmail)
                recyclerView.adapter = friendAcclistAdapter
                friendAcclistAdapter.notifyDataSetChanged()
            }
        }

        return view
    }

}