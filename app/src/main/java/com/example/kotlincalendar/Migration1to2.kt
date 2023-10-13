package com.example.kotlincalendar

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE frdadd_db ADD COLUMN new_column_name TEXT")
    // 여기에 이전 버전(1)에서 새 버전(2)으로의 마이그레이션 로직을 작성
    }
}