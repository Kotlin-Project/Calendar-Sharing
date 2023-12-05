package com.example.kotlincalendar.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var Email:String,
    var Password:String,
    var Name:String,
    var PhoneNum:String,
    var brith_date:String,
    var Profile_img:String,
    //val Profile_img: Bitmap?=null,
    var SubTitle:String,
)