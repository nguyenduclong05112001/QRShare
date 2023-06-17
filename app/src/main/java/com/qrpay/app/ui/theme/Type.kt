package com.qrpay.app.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

private val fontFamily = FontFamily.Default

val textFieldStyle = TextStyle(
    color = white,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    textAlign = TextAlign.Start,
)

val textStyle16Bold = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    textAlign = TextAlign.Center
)

val textStyle24Light = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    textAlign = TextAlign.Center
)

val placeholderStyle = TextStyle(
    color = gray,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    textAlign = TextAlign.Start,
)