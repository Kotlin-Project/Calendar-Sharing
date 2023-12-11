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

    @Query("UPDATE User SET SubTitle = :newStatus WHERE Email = :statusUserEmail")
    fun updateStatus(statusUserEmail: String, newStatus: String)

    /*    @Query("SELECT * FROM ShareCalendarUser WHERE ShareCalendarId=:shareCalendarId AND masterUser=1")
        fun getMasterUserInfo(shareCalendarId:String):User

        @Query("SELECT * FROM ShareCalendarUser WHERE ShareCalendarId=:shareCalendarId AND masterUser=0")
        fun getMemberUserInfo(shareCalendarId:String):List<User>*/
    @Query("SELECT User.Email, User.Password, User.Name, User.PhoneNum, User.brith_date, User.Profile_img, User.SubTitle FROM User INNER JOIN ShareCalendarUser ON ShareCalendarUser.userEmail = User.Email WHERE ShareCalendarUser.shareCalendarId = :calendarId AND ShareCalendarUser.masterUser = 1")
    fun getMasterUserInfo(calendarId: String): User

    @Query("SELECT User.Email, User.Password, User.Name, User.PhoneNum, User.brith_date, User.Profile_img, User.SubTitle FROM User INNER JOIN ShareCalendarUser ON ShareCalendarUser.userEmail = User.Email WHERE ShareCalendarUser.shareCalendarId = :calendarId AND ShareCalendarUser.masterUser = 0")
    fun getMemberUserInfo(calendarId: String): List<User>

    @Query("UPDATE User SET Profile_img = :newProfile WHERE Email = :profileUser")
    fun updateProfile(profileUser: String, newProfile: String)
}