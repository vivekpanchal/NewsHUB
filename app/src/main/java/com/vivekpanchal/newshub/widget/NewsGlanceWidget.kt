package com.vivekpanchal.newshub.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import coil.imageLoader
import coil.request.ImageRequest
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.ui.MainActivity

/** Compose-first replacement for the legacy RemoteViews-based `NewsWidgetProvider`. */
class NewsGlanceWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition
    override val sizeMode = SizeMode.Single

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<Preferences>()
            val headline = state[WidgetPrefsKeys.HEADLINE] ?: context.getString(R.string.appwidget_news_headline_text)
            val date = state[WidgetPrefsKeys.DATE] ?: context.getString(R.string.appwidget_news_date_text)
            val imageUrl = state[WidgetPrefsKeys.IMAGE_URL]
            val bitmap: Bitmap? = imageUrl?.let { loadBitmap(context, it) }

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(12.dp)
                    .clickable(actionStartActivity<MainActivity>()),
            ) {
                if (bitmap != null) {
                    Image(
                        provider = ImageProvider(bitmap),
                        contentDescription = context.getString(R.string.appwidget_image_desc),
                        contentScale = ContentScale.Crop,
                        modifier = GlanceModifier.fillMaxSize(),
                    )
                } else {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .background(ColorProvider(R.color.colorPrimaryDark)),
                    ) {}
                }

                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(ColorProvider(R.color.widget_scrim))
                        .padding(8.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = headline,
                        style = TextStyle(color = ColorProvider(android.R.color.white)),
                        maxLines = 2,
                    )
                    Text(
                        text = date,
                        style = TextStyle(color = ColorProvider(android.R.color.white)),
                    )
                }
            }
        }
    }

    private suspend fun loadBitmap(context: Context, url: String): Bitmap? = runCatching {
        val request = ImageRequest.Builder(context).data(url).allowHardware(false).build()
        val result = context.imageLoader.execute(request)
        (result.drawable as? BitmapDrawable)?.bitmap
    }.getOrNull()
}
