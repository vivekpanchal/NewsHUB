package com.vivekpanchal.newshub.widget

import androidx.datastore.preferences.core.stringPreferencesKey

object WidgetPrefsKeys {
    val HEADLINE = stringPreferencesKey("widget_headline")
    val DATE = stringPreferencesKey("widget_date")
    val IMAGE_URL = stringPreferencesKey("widget_image_url")
}
