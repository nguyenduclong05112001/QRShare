package com.qrpay.app.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.longhrk.dimension.R
import com.qrpay.app.core.GetDimension
import com.qrpay.app.ui.theme.black
import com.qrpay.app.ui.theme.textStyle16Bold
import com.qrpay.app.ui.theme.white

@Composable
fun TextButtonCorner(modifier: Modifier, textContent: String, onClick: () -> Unit) {
    Box(modifier = modifier.fillMaxWidth()) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(15),
            border = BorderStroke(1.dp, white)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)
                    .padding(vertical = 10.dp),
                text = textContent,
                color = white,
                fontSize = GetDimension.dimensionOfText(res = R.dimen._12sdp),
                style = textStyle16Bold
            )
        }
    }
}