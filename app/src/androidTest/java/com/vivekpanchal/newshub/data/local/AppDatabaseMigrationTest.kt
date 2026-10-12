package com.vivekpanchal.newshub.data.local

import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {

    @Test
    fun migratesLegacyV1FavoritesAndDropsDuplicateUrls() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val name = "migration-test"
        context.deleteDatabase(name)
        // Legacy Java app's v1 schema: nullable columns, no index.
        SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(name), null).apply {
            execSQL(
                "CREATE TABLE news_headlines (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, news_headline TEXT, " +
                    "date TEXT, image_url TEXT, author_name TEXT, description TEXT, news_source TEXT, news_url TEXT)",
            )
            execSQL("INSERT INTO news_headlines (news_headline, date, news_url) VALUES ('Old', NULL, 'u1')")
            execSQL("INSERT INTO news_headlines (news_headline, date, news_url) VALUES ('New', 'd', 'u1')")
            execSQL("INSERT INTO news_headlines (news_headline, date, news_url) VALUES ('NoUrl', 'd', NULL)")
            version = 1
            close()
        }

        val db = Room.databaseBuilder(context, AppDatabase::class.java, name)
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
        val headlines = db.newsHeadlineDao().loadAllNewsHeadlines().first().map { it.headline }
        db.close()

        assertEquals(listOf("NoUrl", "New"), headlines)
    }
}
