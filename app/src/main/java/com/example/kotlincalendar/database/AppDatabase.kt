package com.example.kotlincalendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, frdadd_db::class, frdlist_db::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): User_Dao
    abstract fun frdaddDbDao(): frdadd_db_Dao
    abstract fun frdlistDbDao(): frdlist_db_Dao

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