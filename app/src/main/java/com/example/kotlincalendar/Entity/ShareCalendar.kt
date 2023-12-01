package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Entity
data class ShareCalendar(
    @PrimaryKey
    val shareCalendarId:String = UUID.randomUUID().toString(),
    //val masterEmail:String,
    var shareCalendarTitle:String,
    var shareCalendarImage:Int,
    var shareCalendarCategory:String,
    )
