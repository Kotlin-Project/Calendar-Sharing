package com.example.kotlincalendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kotlincalendar.Dao.FriendAddDao
import com.example.kotlincalendar.Dao.FriendListDao
import com.example.kotlincalendar.Dao.UserCalendarDao
import com.example.kotlincalendar.Dao.UserDao
import com.example.kotlincalendar.Entity.FriendAdd
import com.example.kotlincalendar.Entity.FriendList
import com.example.kotlincalendar.Entity.UserCalendar
import com.example.kotlincalendar.Entity.User


@Database(entities = [User::class, FriendAdd::class, FriendList::class, UserCalendar::class], version = 1)
@TypeConverters(LocalDateConverter::class, LocalTimeConverter::class) // TypeConverter 등록
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun friendAddDao(): FriendAddDao
    abstract fun friendListDao(): FriendListDao
    abstract fun userCalendarDao(): UserCalendarDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "Test"
                    ).build()
                }
            }
            return instance
        }
    }
}