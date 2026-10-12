package com.vivekpanchal.newshub.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vivekpanchal.newshub.R
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val OUTPUT_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
private val SHORT_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM")

/** Parses an ISO-8601 timestamp (as returned by newsapi.org). Returns null if it can't be parsed. */
private fun parseNewsInstant(rawDate: String?): Instant? {
    if (rawDate.isNullOrBlank()) return null
    return try {
        OffsetDateTime.parse(rawDate).toInstant()
    } catch (e: DateTimeParseException) {
        try {
            ZonedDateTime.parse(rawDate).toInstant()
        } catch (e2: DateTimeParseException) {
            null
        }
    }
}

/** "dd MMMM yyyy" - used where a full, precise date reads better than a relative one. */
fun formatNewsDate(rawDate: String?): String {
    val instant = parseNewsInstant(rawDate) ?: return rawDate.orEmpty()
    return OUTPUT_FORMAT.format(OffsetDateTime.ofInstant(instant, java.time.ZoneId.systemDefault()))
}

/** Structured result of [relativeTimeOf]; UI layers render this into localizable text via string resources. */
sealed interface RelativeTime {
    data object JustNow : RelativeTime
    data class Minutes(val value: Long) : RelativeTime
    data class Hours(val value: Long) : RelativeTime
    data class Days(val value: Long) : RelativeTime
    data class Absolute(val formatted: String) : RelativeTime
}

/**
 * Pure, Context-free computation behind the wire-desk-style relative timestamp used throughout
 * the feed, reel, and card surfaces. Kept free of Android resources so it stays unit-testable;
 * [formatRelativeTime] renders the result into localizable text.
 */
fun relativeTimeOf(rawDate: String?, now: Instant = Instant.now()): RelativeTime? {
    val instant = parseNewsInstant(rawDate) ?: return null
    val minutes = Duration.between(instant, now).toMinutes().coerceAtLeast(0)
    return when {
        minutes < 1 -> RelativeTime.JustNow
        minutes < 60 -> RelativeTime.Minutes(minutes)
        minutes < 60 * 24 -> RelativeTime.Hours(minutes / 60)
        minutes < 60 * 24 * 7 -> RelativeTime.Days(minutes / (60 * 24))
        else -> RelativeTime.Absolute(
            SHORT_DATE_FORMAT.format(OffsetDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())),
        )
    }
}

/** "Just now" / "12m" / "3h" / "2d", falling back to "07 Aug" beyond a week - see [relativeTimeOf]. */
@Composable
fun formatRelativeTime(rawDate: String?, now: Instant = Instant.now()): String {
    return when (val relative = relativeTimeOf(rawDate, now)) {
        null -> rawDate.orEmpty()
        RelativeTime.JustNow -> stringResource(R.string.date_relative_just_now)
        is RelativeTime.Minutes -> stringResource(R.string.date_relative_minutes_short, relative.value)
        is RelativeTime.Hours -> stringResource(R.string.date_relative_hours_short, relative.value)
        is RelativeTime.Days -> stringResource(R.string.date_relative_days_short, relative.value)
        is RelativeTime.Absolute -> relative.formatted
    }
}

/** Heuristic used for the "Breaking now" rail: no API flags this, so recency stands in for it. */
fun isRecentEnoughToBeBreaking(rawDate: String?, withinMinutes: Long = 45, now: Instant = Instant.now()): Boolean {
    val instant = parseNewsInstant(rawDate) ?: return false
    return Duration.between(instant, now).toMinutes() in 0..withinMinutes
}
