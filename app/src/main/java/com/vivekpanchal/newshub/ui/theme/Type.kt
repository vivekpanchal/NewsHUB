package com.vivekpanchal.newshub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vivekpanchal.newshub.R

/** Headlines, mastheads - editorial weight. Set tight and large, never more than ~9 words on a card. */
val EditorialSerif = FontFamily(Font(R.font.source_serif_bold, FontWeight.Bold))

/** Interface chrome, summaries, buttons, labels - the platform's default sans (Roboto on Android). */
val UiSans = FontFamily.Default

/** Timestamps, counts, rank badges - the wire-service tell. Always available, no network fetch. */
val DataMono = FontFamily.Monospace

private val base = Typography()

val NewsHubTypography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = EditorialSerif, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
    displayMedium = base.displayMedium.copy(fontFamily = EditorialSerif, fontWeight = FontWeight.Bold),
    displaySmall = base.displaySmall.copy(fontFamily = EditorialSerif, fontWeight = FontWeight.Bold),
    headlineLarge = base.headlineLarge.copy(fontFamily = EditorialSerif, fontWeight = FontWeight.Bold),
    headlineMedium = base.headlineMedium.copy(fontFamily = EditorialSerif, fontWeight = FontWeight.Bold),
    headlineSmall = base.headlineSmall.copy(fontFamily = EditorialSerif, fontWeight = FontWeight.Bold),
    titleLarge = base.titleLarge.copy(fontFamily = EditorialSerif, fontWeight = FontWeight.Bold),
    titleMedium = base.titleMedium.copy(fontFamily = UiSans, fontWeight = FontWeight.Bold),
    titleSmall = base.titleSmall.copy(fontFamily = UiSans, fontWeight = FontWeight.Bold),
    bodyLarge = base.bodyLarge.copy(fontFamily = UiSans),
    bodyMedium = base.bodyMedium.copy(fontFamily = UiSans),
    bodySmall = base.bodySmall.copy(fontFamily = UiSans),
    labelLarge = base.labelLarge.copy(fontFamily = UiSans, fontWeight = FontWeight.Bold),
    labelMedium = base.labelMedium.copy(fontFamily = UiSans, fontWeight = FontWeight.Bold),
    labelSmall = base.labelSmall.copy(fontFamily = UiSans, fontWeight = FontWeight.Bold),
)

/**
 * Data/wire-service text style - timestamps, engagement counts, rank numbers. Not part of Material3's
 * Typography slots, so it's exposed separately; reach for it wherever the design calls for the mono face.
 */
object NewsHubExtraType {
    val eyebrow: TextStyle = TextStyle(
        fontFamily = DataMono,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 1.4.sp,
    )
    val data: TextStyle = TextStyle(
        fontFamily = DataMono,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.2.sp,
    )
}
