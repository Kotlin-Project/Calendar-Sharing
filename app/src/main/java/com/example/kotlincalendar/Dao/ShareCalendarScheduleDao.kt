package com.example.kotlincalendar.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kotlincalendar.Entity.ShareCalendarSchedule
import com.example.kotlincalendar.Entity.UserCalendar
import java.time.LocalDate

@Dao
interface ShareCalendarScheduleDao {
    @Insert
    fun insertShareSchedule(shareSchedule: ShareCalendarSchedule)
    @Delete
    fun deleteShareSchedule(shareSchedule: ShareCalendarSchedule)
    @Update
    fun updateShareSchedule(shareSchedule: ShareCalendarSchedule)

    @Query("SELECT * FROM ShareCalendarSchedule WHERE shareCalendarId = :shareCalendarId ORDER BY shareScheduleStart")
    fun getShareScheduleId(shareCalendarId: String?): List<ShareCalendarSchedule>

    @Query("SELECT * FROM ShareCalendarSchedule WHERE shareCalendarId = :shareCalendarId AND shareScheduleLocalDate = :selectedDate ORDER BY shareScheduleStart")
    fun getShareScheduleForUser(shareCalendarId: String, selectedDate: LocalDate): List<ShareCalendarSchedule>

    @Query("SELECT * FROM ShareCalendarSchedule WHERE shareScheduleId=:SelectedId")
    fun getScheduleItemById(SelectedId: Long):List<ShareCalendarSchedule>

}