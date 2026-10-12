package com.vivekpanchal.newshub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [NewsHeadlineEntity::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsHeadlineDao(): NewsHeadlineDao

    companion object {
        const val DATABASE_NAME = "NewsHUB"

        /**
         * v1 shipped with two different schemas under the same version number: the legacy Java app
         * (nullable columns, no index) and the Compose rewrite (unique headline index). Rebuilding
         * the table works from either, keeps every favorite, and drops duplicate URLs.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE news_headlines_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "news_headline TEXT NOT NULL, date TEXT NOT NULL, image_url TEXT, author_name TEXT, " +
                        "description TEXT, news_source TEXT, news_url TEXT)",
                )
                db.execSQL("CREATE UNIQUE INDEX index_news_headlines_news_url ON news_headlines_new (news_url)")
                db.execSQL(
                    "INSERT OR IGNORE INTO news_headlines_new " +
                        "SELECT id, COALESCE(news_headline, ''), COALESCE(date, ''), image_url, author_name, " +
                        "description, news_source, news_url FROM news_headlines ORDER BY id DESC",
                )
                db.execSQL("DROP TABLE news_headlines")
                db.execSQL("ALTER TABLE news_headlines_new RENAME TO news_headlines")
            }
        }
    }
}
