package com.example.kotlincalendar.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime


class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate): String {
        return localDate.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }
}

class LocalTimeConverter {
    @TypeConverter
    fun fromLocalTime(localTime: LocalTime): String {
        return localTime.toString()
    }

    @TypeConverter
    fun toLocalTime(value: String): LocalTime {
        return LocalTime.parse(value)
    }
}