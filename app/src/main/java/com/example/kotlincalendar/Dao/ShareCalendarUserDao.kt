package com.example.kotlincalendar.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlincalendar.Entity.ShareCalendarUser

@Dao
interface ShareCalendarUserDao {
    @Insert
    fun joinShareCalendar(shareCalendarUser: ShareCalendarUser)
    @Delete
    fun kickUser(shareCalendarUser: ShareCalendarUser)
    @Query("SELECT shareCalendarId FROM ShareCalendarUser WHERE userEmail = :userEmail")
    fun getShareCalendarIdsForUser(userEmail: String): List<String>
    @Query("DELETE FROM ShareCalendarUser WHERE shareCalendarId = :shareCalendarId")
    fun deleteShareCalendarUsersByCalendarId(shareCalendarId: String)
    @Query("SELECT * FROM ShareCalendarUser WHERE shareCalendarId=:shareCalendarId AND userEmail=:userEmail")
    fun selectedUser(shareCalendarId: String, userEmail: String) :ShareCalendarUser?
    @Query("DELETE FROM ShareCalendarUser WHERE shareCalendarId=:shareCalendarId AND userEmail=:userEmail")
    fun secessionUserGetCalendarId(shareCalendarId: String, userEmail: String)
    @Query("SELECT masterUser FROM ShareCalendarUser WHERE shareCalendarId=:shareCalendarId AND userEmail=:userEmail")
    fun getMasterUserEmail(shareCalendarId:String,userEmail: String): Boolean

  /*  @Query("SELECT ShareCalendarUser.*, User.Name, User.Profile_img FROM ShareCalendarUser INNER JOIN User ON ShareCalendarUser.userEmail = User.Email WHERE ShareCalendarUser.shareCalendarId = :calendarId AND ShareCalendarUser.masterUser = 1")
    suspend fun getMasterUsersInformation(calendarId: String): List<ShareCalendarUserWithUserInfo>

    @Query("SELECT ShareCalendarUser.*, User.Name, User.Profile_img FROM ShareCalendarUser INNER JOIN User ON ShareCalendarUser.userEmail = User.Email WHERE ShareCalendarUser.shareCalendarId = :calendarId AND ShareCalendarUser.masterUser = 0")
    suspend fun getNonMasterUsersInformation(calendarId: String): List<ShareCalendarUserWithUserInfo>
*/}