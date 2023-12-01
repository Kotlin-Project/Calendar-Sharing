package com.example.kotlincalendar.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.kotlincalendar.Entity.ShareCalendar
import com.example.kotlincalendar.Entity.ShareCalendarUser

@Dao
interface ShareCalendarDao {
    /*@Insert
    fun insertShareCalendar(shareCalendar: ShareCalendar)

    @Delete
    fun deleteShareCalendar(shareCalendar: ShareCalendar)*/

    @Insert
    fun insertShareCalendar(shareCalendar: ShareCalendar):Long

    @Transaction
    fun insertShareCalendarAndUser(shareCalendar: ShareCalendar, shareCalendarUser: ShareCalendarUser, shareCalendarUserDao: ShareCalendarUserDao) {
        val calendarId = insertShareCalendar(shareCalendar)
        shareCalendarUserDao.joinShareCalendar(shareCalendarUser.copy(shareCalendarId = calendarId.toString()))
    }

    @Query("SELECT * FROM ShareCalendar WHERE shareCalendarId IN (:shareCalendarIds)")
    fun getShareCalendarsByIds(shareCalendarIds: List<String>): List<ShareCalendar>
}
