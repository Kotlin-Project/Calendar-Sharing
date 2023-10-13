package com.example.kotlincalendar.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val Email:String,
    val Password:String,
    val Name:String,
    val PhoneNum:String,
    val brith_date:String,
    val Profile_img:Int,
    //val Profile_img: Bitmap?=null,
    val SubTitle:String,
){
    fun getUserEmail():String{
        return Email
    }
}
@Entity
data class frdlist_db(
    @PrimaryKey(autoGenerate=true)
    val Frdlist_ID:Long,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["User1"])
    val User1:String,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["User2"])
    val User2:String,
)

@Entity
data class frdadd_db(
    @PrimaryKey(autoGenerate=true)
    val RequestID:Long,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["Sender_ID"])
    val Sender_ID:String,
    @ForeignKey(entity= User::class, parentColumns = ["Email"], childColumns = ["Receiver_ID"])
    val Receiver_ID:String,
)
