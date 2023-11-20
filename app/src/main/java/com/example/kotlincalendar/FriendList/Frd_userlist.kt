package com.example.kotlincalendar.FriendList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.FragmentFrdUserlistBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Frd_userlist : Fragment() {
    private var mBinding: FragmentFrdUserlistBinding? = null
    private val binding get() = mBinding!!
    var db: AppDatabase? = null

    companion object {
        private const val USER_EMAIL_KEY = "user_email"
        fun newInstance(userEmail: String): Frd_userlist {
            val fragment = Frd_userlist()
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
        mBinding = FragmentFrdUserlistBinding.inflate(inflater, container, false)
        val view = binding.root
        val userEmail = arguments?.getString(USER_EMAIL_KEY).toString()
        Log.d("Frd_userlist", "User email: $userEmail")

        val recyclerView=binding.userlistFrdView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        db = AppDatabase.getInstance(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val friendsList = db?.frdlistDbDao()?.getFriends(userEmail)
            val sentRequests = db?.frdaddDbDao()?.getSentRequests(userEmail)
            val receivedRequests = db?.frdaddDbDao()?.getRequests(userEmail)
            val allUsers = db?.userDao()?.Select_frd()

            val filterUsers = allUsers!!.filter { user ->
                !friendsList?.any { it.User1 == user.Email || it.User2 == user.Email }!! &&
                        !sentRequests?.any { it.Receiver_ID == user.Email }!! &&
                        !receivedRequests?.any { it.Sender_ID == user.Email }!! &&
                        user.Email!=userEmail
            }
            withContext(Dispatchers.Main) {
                val frienduserlistAdapter = Frd_userlist_Adapter(requireContext(),filterUsers, userEmail)
                recyclerView.adapter = frienduserlistAdapter
                frienduserlistAdapter.notifyDataSetChanged()
            }
        }
        return view
    }
}