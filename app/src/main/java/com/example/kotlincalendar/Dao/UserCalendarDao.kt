package com.example.kotlincalendar.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kotlincalendar.Entity.UserCalendar
import java.time.LocalDate
@Dao
interface UserCalendarDao{
    @Insert
    fun insertSchedule(Scehdule: UserCalendar)
    @Delete
    fun deleteSchedule(Scehdule: UserCalendar)
    @Update
    fun updateSchedule(Scehdule: UserCalendar)
    @Query("SELECT * FROM UserCalendar WHERE Calendar_UserEmail = :userEmail AND Schedule_LocalDate = :selectedDate ORDER BY Schedule_Start")
    fun getScheduleForUser(userEmail: String?, selectedDate: LocalDate): List<UserCalendar>
    @Query("SELECT * FROM UserCalendar WHERE Schedule_ID=:SelectedId")
    fun getCalendarItemById(SelectedId: Int):List<UserCalendar>
    @Query("SELECT * FROM UserCalendar WHERE Calendar_UserEmail = :userEmail ORDER BY Schedule_Start")
    fun getScheduleUser(userEmail: String?): List<UserCalendar>
}
