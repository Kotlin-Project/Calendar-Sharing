package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class ShareCalendarSchedule(
    @PrimaryKey(autoGenerate = true)
    val shareScheduleId: Long,
/*    @ForeignKey(entity = ShareCalendar::class,
        parentColumns = ["shareCalendarId"],
        childColumns = ["shareCalendarId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE)
    @Index("shareCalendarId")*/
    val shareCalendarId: String,
    @ForeignKey(entity = User::class, parentColumns = ["Email"], childColumns = ["userEmail"])
    val userEmail: String,
    var shareScheduleTitle: String,
    var shareScheduleMemo: String,
    var shareScheduleColor: String,
    var shareScheduleLocalDate: LocalDate,
    var shareScheduleStart: LocalTime,
    var shareScheduleEnd: LocalTime,
    var shareAlarm: String,
)
