package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class FriendAdd(
    @PrimaryKey(autoGenerate=true)
    val RequestID:Long,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["Sender_ID"])
    val Sender_ID:String,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["Receiver_ID"])
    val Receiver_ID:String,
)