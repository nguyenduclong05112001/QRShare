package com.qrpay.app.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.longhrk.dimension.R
import com.qrpay.app.core.GetDimension
import com.qrpay.app.ui.theme.white

@Composable
fun HeaderApp(
    modifier: Modifier,
    icon: Int,
    content: String = "",
    onButtonClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.clickable {
                onButtonClick()
            },
            painter = painterResource(id = icon),
            tint = white,
            contentDescription = null
        )

        if (content.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(horizontal = GetDimension.dimensionOf(res = R.dimen._20sdp))
                    .weight(1f)
            ) {
                Text(
                    text = content,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = GetDimension.dimensionOfText(res = R.dimen._16sdp),
                    )
                )
            }
        }
    }
}