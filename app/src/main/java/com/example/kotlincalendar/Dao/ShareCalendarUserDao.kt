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
}