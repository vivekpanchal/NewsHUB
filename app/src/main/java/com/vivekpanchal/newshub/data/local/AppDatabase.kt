package com.vivekpanchal.newshub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsHeadlineEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsHeadlineDao(): NewsHeadlineDao

    companion object {
        const val DATABASE_NAME = "NewsHUB"
    }
}
