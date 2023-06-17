package com.qrpay.app.ui.elements

import android.graphics.Color
import android.os.Build
import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.longhrk.dimension.R
import com.qrpay.app.core.GetDimension
import com.qrpay.app.ui.theme.white
import com.qrpay.app.ui.viewmodel.QRPayViewModel
import jp.wasabeef.blurry.Blurry

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ContentOfQRCode(
    modifier: Modifier,
    qrPayViewModel: QRPayViewModel,
) {
    val isCreated by qrPayViewModel.isCreatedQRCode.collectAsState()
    val isShowLoadingView by qrPayViewModel.isShowLoadingView.collectAsState()
    val context = LocalContext.current

    BackHandler {
        if (isCreated) qrPayViewModel.updateStatusCreatedQR(false)
    }

    val view = LocalView.current
    val bitmap by qrPayViewModel.bitmapQRCode.collectAsState()

    val localDensity = LocalDensity.current

    var widthScreen by remember {
        mutableStateOf(0.dp)
    }

    Box(modifier = modifier
        .fillMaxSize()
        .onGloballyPositioned {
            widthScreen = with(localDensity) {
                it.size.width.toDp()
            }
        }) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                ImageView(context).apply {
                    Blurry.with(context).capture(view).into(this)
                    setColorFilter(Color.parseColor("#7F000000"))
                }
            }
        )

        AndroidView(
            modifier = Modifier
                .size((widthScreen / 1.5f))
                .align(Alignment.Center),
            factory = {
                ImageView(it).apply {
                    Glide.with(it)
                        .load(bitmap)
                        .centerCrop()
                        .into(this)
                }
            })

        Row(
            modifier = Modifier
                .padding(
                    all = GetDimension.dimensionOf(res = R.dimen._10sdp)
                )
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ButtonOption(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = GetDimension.dimensionOf(res = R.dimen._5sdp)),
                color = white.copy(alpha = 0.1f),
                icon = com.qrpay.app.R.drawable.ic_share,
                textContent = "Share"
            ) {
                bitmap?.let {
                    qrPayViewModel.shareOtherApp(
                        context = context,
                        bitmap = it
                    )
                }
            }

            ButtonOption(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = GetDimension.dimensionOf(res = R.dimen._5sdp)),
                color = white.copy(alpha = 0.1f),
                icon = com.qrpay.app.R.drawable.ic_save,
                textContent = "Save"
            ) {
                bitmap?.let { qrPayViewModel.saveInStorage(context = context, bitmap = it) }
            }
        }

        if (isShowLoadingView) {
            LoadingView(false)
        }
    }
}