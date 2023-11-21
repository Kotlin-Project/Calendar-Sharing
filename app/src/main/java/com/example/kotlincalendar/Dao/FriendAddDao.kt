package com.example.kotlincalendar.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlincalendar.Entity.FriendAdd

@Dao
interface FriendAddDao{
    @Insert
    fun insertRequest(frdaddDB: FriendAdd)

    @Delete
    fun deleteRequest(frdaddDB: FriendAdd)

    @Query("SELECT * FROM FriendAdd WHERE Receiver_ID = :userId")
    fun getRequests(userId: String): List<FriendAdd>

    @Query("SELECT * FROM FriendAdd WHERE Sender_ID = :userId")
    fun getSentRequests(userId: String): List<FriendAdd>
}