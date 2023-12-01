package com.example.kotlincalendar.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kotlincalendar.Entity.FriendList
import com.example.kotlincalendar.Entity.FriendAdd
import com.example.kotlincalendar.Entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun Select_frd(): List<User>

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM User WHERE Email = :email")
    fun getUserId(email: String?): List<User>

    @Query("SELECT * FROM FriendList WHERE (user1=:userEmail AND User2=:friendEmail) OR (User1=:friendEmail AND User2=:userEmail)")
    fun areFriends(userEmail: String, friendEmail: String): FriendList?

    @Query("SELECT *FROM FriendAdd WHERE (Sender_ID=:userEmail AND Receiver_ID=:friendEmail)")
    fun isRequestPending(userEmail: String, friendEmail: String): FriendAdd?

    @Query("SELECT * FROM User WHERE Email = :email AND Password = :password")
    fun get_Login(email: String, password: String): User?

    @Query("SELECT * FROM User WHERE Email=:email")
    fun getUserByEmail(email: String): User?

    @Query("SELECT Name FROM User WHERE Email = :userEmail")
    fun getUserNameByEmail(userEmail: String?): String?

    @Query("SELECT Name FROM User WHERE SubTitle = :userStatus")
    fun getUserSubTitle(userStatus: String?): String?

    @Query("UPDATE User SET SubTitle = :newStatus WHERE Email = :statusUserEmail")
    fun updateStatus(statusUserEmail: String, newStatus: String)
}