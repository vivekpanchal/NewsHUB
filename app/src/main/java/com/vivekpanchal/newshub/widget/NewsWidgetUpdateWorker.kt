package com.vivekpanchal.newshub.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vivekpanchal.newshub.data.repository.NewsRepository
import com.vivekpanchal.newshub.data.repository.NewsResult
import com.vivekpanchal.newshub.util.formatNewsDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/** Periodically refreshes the home-screen widget, replacing the legacy `NewsUpdateService` IntentService. */
@HiltWorker
class NewsWidgetUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val newsRepository: NewsRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val result = newsRepository.getTopHeadlines()
        val article = (result as? NewsResult.Success)?.articles?.firstOrNull()
            // Leave the widget showing its last-known headline rather than burning battery/network
            // retrying on every periodic run (e.g. while NEWS_API_KEY is unset or the network is down).
            ?: return Result.success()

        val glanceIds = GlanceAppWidgetManager(applicationContext).getGlanceIds(NewsGlanceWidget::class.java)
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(applicationContext, glanceId) { prefs ->
                prefs[WidgetPrefsKeys.HEADLINE] = article.headline
                prefs[WidgetPrefsKeys.DATE] = formatNewsDate(article.publishedAt)
                article.imageUrl?.let { prefs[WidgetPrefsKeys.IMAGE_URL] = it }
            }
            NewsGlanceWidget().update(applicationContext, glanceId)
        }
        return Result.success()
    }

    companion object {
        const val UNIQUE_WORK_NAME = "news_widget_update_work"
    }
}
