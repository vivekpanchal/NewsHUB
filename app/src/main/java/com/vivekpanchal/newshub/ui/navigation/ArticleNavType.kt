package com.vivekpanchal.newshub.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.vivekpanchal.newshub.domain.model.Article

/** Lets an [Article] travel as a single Navigation-Compose route argument. */
object ArticleNavType : NavType<Article>(isNullableAllowed = false) {

    private val gson = Gson()

    override fun get(bundle: Bundle, key: String): Article? =
        androidx.core.os.BundleCompat.getParcelable(bundle, key, Article::class.java)

    override fun parseValue(value: String): Article = gson.fromJson(Uri.decode(value), Article::class.java)

    override fun serializeAsValue(value: Article): String = Uri.encode(gson.toJson(value))

    override fun put(bundle: Bundle, key: String, value: Article) = bundle.putParcelable(key, value)
}
