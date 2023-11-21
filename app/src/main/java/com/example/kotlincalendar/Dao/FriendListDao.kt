package com.example.kotlincalendar.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlincalendar.Entity.FriendList

@Dao
interface FriendListDao{
    @Insert
    fun insertFriend(friend: FriendList)

    @Delete
    fun deleteFriend(friend: FriendList)

    @Query("SELECT * FROM FriendList WHERE user1 = :userId OR user2 = :userId")
    fun getFriends(userId: String): List<FriendList>
}