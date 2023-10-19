package com.example.kotlincalendar.Calendar

import java.time.LocalDate
import java.util.Calendar
import java.util.TimeZone

class CalendarUtil {
    companion object{
        var selectedDate: Calendar=Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    }
}