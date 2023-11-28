package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class UserCalendar(
    @PrimaryKey(autoGenerate=true)
    val Schedule_ID:Int,
    @ForeignKey(entity=User::class, parentColumns = ["Email"], childColumns = ["Calendar_UserEmail"])
    val Calendar_UserEmail:String?,
    var Schedule_Title:String,
    var Schedule_Memo:String,
    var Schedule_Color:String,
    var Schedule_LocalDate: LocalDate,
    var Schedule_Start: LocalTime,
    var Schedule_End: LocalTime,
    var alarm: String,
)