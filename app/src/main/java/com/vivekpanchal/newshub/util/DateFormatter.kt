package com.vivekpanchal.newshub.util

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

/**
 * "Just now" / "12m" / "3h" / "2d", falling back to "07 Aug" beyond a week - the wire-desk-style
 * relative timestamp used throughout the feed, reel, and card surfaces.
 */
fun formatRelativeTime(rawDate: String?, now: Instant = Instant.now()): String {
    val instant = parseNewsInstant(rawDate) ?: return rawDate.orEmpty()
    val minutes = Duration.between(instant, now).toMinutes().coerceAtLeast(0)
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m"
        minutes < 60 * 24 -> "${minutes / 60}h"
        minutes < 60 * 24 * 7 -> "${minutes / (60 * 24)}d"
        else -> SHORT_DATE_FORMAT.format(OffsetDateTime.ofInstant(instant, java.time.ZoneId.systemDefault()))
    }
}

/** Heuristic used for the "Breaking now" rail: no API flags this, so recency stands in for it. */
fun isRecentEnoughToBeBreaking(rawDate: String?, withinMinutes: Long = 45, now: Instant = Instant.now()): Boolean {
    val instant = parseNewsInstant(rawDate) ?: return false
    return Duration.between(instant, now).toMinutes() in 0..withinMinutes
}
