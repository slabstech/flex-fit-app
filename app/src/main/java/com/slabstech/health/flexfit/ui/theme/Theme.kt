package com.slabstech.health.flexfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define your custom colors
private val FlexFitColorScheme = lightColorScheme(
    primary = Color(0xFFFF3B30),      // Red-Orange (Duolingo style)
    secondary = Color(0xFFFF9500),
    tertiary = Color(0xFFFFD60A),
    background = Color(0xFFF2F2F7),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

// Optional: Customize typography (or just use default)
private val FlexFitTypography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp
//    )
    // Add custom styles here if needed
)

@Composable
fun FlexFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FlexFitColorScheme,
        typography = FlexFitTypography,        // Correct Material3 Typography
        content = content
    )
}