package com.qrpay.app.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.longhrk.dimension.R
import com.qrpay.app.core.GetDimension
import com.qrpay.app.ui.theme.textStyle24Light
import com.qrpay.app.ui.theme.white

@Composable
fun ButtonOption(
    modifier: Modifier,
    textContent: String,
    color: Color = white,
    onListener: () -> Unit
) {
    val localDensity = LocalDensity.current

    var widthOfButton by remember {
        mutableStateOf(0.dp)
    }

    Box(modifier = modifier
        .fillMaxWidth()
        .onGloballyPositioned {
            widthOfButton = with(localDensity) { it.size.width.toDp() }
        }
        .height(height = (widthOfButton / 2))
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            colors = ButtonDefaults.textButtonColors(color),
            shape = RoundedCornerShape(15),
            onClick = onListener
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = textContent,
                color = white,
                fontSize = GetDimension.dimensionOfText(res = R.dimen._16sdp),
                style = textStyle24Light
            )
        }
    }
}

@Composable
fun ButtonOption(
    modifier: Modifier,
    textContent: String,
    color: Color = white,
    icon: Int,
    onListener: () -> Unit
) {
    val localDensity = LocalDensity.current

    var widthOfButton by remember {
        mutableStateOf(0.dp)
    }

    Box(modifier = modifier
        .fillMaxWidth()
        .onGloballyPositioned {
            widthOfButton = with(localDensity) { it.size.width.toDp() }
        }
        .height(height = (widthOfButton / 2))
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            colors = ButtonDefaults.textButtonColors(color),
            shape = RoundedCornerShape(10),
            onClick = onListener
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.weight(1f),
                    tint = white,
                    painter = painterResource(id = icon),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = textContent,
                    color = white,
                    fontSize = GetDimension.dimensionOfText(res = R.dimen._16sdp),
                    style = textStyle24Light
                )
            }
        }
    }
}