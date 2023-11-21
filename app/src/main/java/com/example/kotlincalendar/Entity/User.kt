package com.example.kotlincalendar.Entity

import androidx.room.Entity
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
)