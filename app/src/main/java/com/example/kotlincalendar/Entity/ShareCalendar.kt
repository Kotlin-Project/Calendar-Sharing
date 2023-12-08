package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.UUID

@Entity
data class ShareCalendar(
    @PrimaryKey
    val shareCalendarId:String = UUID.randomUUID().toString(),
    //val masterEmail:String,
    var shareCalendarTitle:String,
    var shareCalendarSubTitle:String,
    var shareCalendarImage:Int,
    var shareCalendarCategory:String,
    val createdAt: Long = Calendar.getInstance().timeInMillis // 생성 시간을 저장하는 필드
    )
