package com.vivekpanchal.newshub.util

import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val OUTPUT_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

/** Parses an ISO-8601 timestamp (as returned by newsapi.org) into "dd MMMM yyyy". */
fun formatNewsDate(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) return ""
    return try {
        OffsetDateTime.parse(rawDate).format(OUTPUT_FORMAT)
    } catch (e: DateTimeParseException) {
        try {
            ZonedDateTime.parse(rawDate).format(OUTPUT_FORMAT)
        } catch (e2: DateTimeParseException) {
            rawDate
        }
    }
}
