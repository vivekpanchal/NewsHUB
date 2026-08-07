package com.vivekpanchal.newshub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.vivekpanchal.newshub.R

val RalewayFontFamily = FontFamily(
    Font(R.font.raleway_bold, FontWeight.Bold),
    Font(R.font.raleway_black, FontWeight.Black),
)

val NewsHubTypography = Typography(
    headlineSmall = Typography().headlineSmall.copy(fontFamily = RalewayFontFamily, fontWeight = FontWeight.Bold),
    titleLarge = Typography().titleLarge.copy(fontFamily = RalewayFontFamily, fontWeight = FontWeight.Bold),
    titleMedium = Typography().titleMedium.copy(fontFamily = RalewayFontFamily, fontWeight = FontWeight.Bold),
)
