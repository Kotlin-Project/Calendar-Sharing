package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime


@Entity(foreignKeys = [
    ForeignKey(entity = ShareCalendar::class, parentColumns = ["shareCalendarId"], childColumns = ["shareCalendarId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = User::class, parentColumns = ["Email"], childColumns = ["userEmail"])
])
data class ShareCalendarSchedule(
    @PrimaryKey(autoGenerate = true)
    val shareScheduleId: Long = 0,
    val shareCalendarId: String,
    val userEmail: String,
    var shareScheduleTitle: String,
    var shareScheduleMemo: String,
    var shareScheduleColor: String,
    var shareScheduleLocalDate: LocalDate,
    var shareScheduleStart: LocalTime,
    var shareScheduleEnd: LocalTime,
    var shareAlarm: String,
)
