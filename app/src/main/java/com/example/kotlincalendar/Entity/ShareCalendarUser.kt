package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["userEmail", "shareCalendarId"])
data class ShareCalendarUser(
    @ForeignKey(
        entity= ShareCalendar::class,
        parentColumns = ["shareCalendarId"],
        childColumns = ["shareCalendarId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )
    val shareCalendarId: String,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["userEmail"])
    val userEmail: String,
    val masterUser:Boolean,
    val canManagerEvent:Boolean,
    val canEvent: Boolean,
)