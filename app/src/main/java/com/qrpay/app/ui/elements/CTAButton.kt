package com.qrpay.app.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.longhrk.dimension.R
import com.qrpay.app.core.GetDimension
import com.qrpay.app.ui.theme.black
import com.qrpay.app.ui.theme.white

@Composable
fun CTAButton(
    modifier: Modifier,
    content: String,
    onClickButton: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClickButton,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = black,
            contentColor = white
        ),
        contentPadding = PaddingValues(
            horizontal = GetDimension.dimensionOf(res = R.dimen._10sdp),
            vertical = GetDimension.dimensionOf(res = R.dimen._20sdp),
        )
    ) {
        Text(
            text = content,
            fontSize = GetDimension.dimensionOfText(res = R.dimen._16sdp),
            fontWeight = FontWeight.Bold
        )
    }
}