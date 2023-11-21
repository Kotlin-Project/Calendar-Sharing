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
    val Schedule_Title:String,
    val Schedule_Memo:String,
    val Schedule_Color:String,
    val Schedule_LocalDate: LocalDate,
    val Schedule_Start: LocalTime,
    val Schedule_End: LocalTime,
    val alarm: String,
)