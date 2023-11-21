package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class FriendList(
    @PrimaryKey(autoGenerate=true)
    val Frdlist_ID:Long,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["User1"])
    val User1:String,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["User2"])
    val User2:String,
)