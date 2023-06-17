package com.qrpay.app.ui.screen

import android.os.Build
import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.qrpay.app.core.GetDimension
import com.qrpay.app.ui.elements.*
import com.qrpay.app.ui.theme.black
import com.qrpay.app.ui.theme.white
import com.qrpay.app.ui.viewmodel.QRPayViewModel
import com.qrpay.data.model.QRCodeContent
import jp.wasabeef.blurry.Blurry
import com.longhrk.dimension.R as resDimension
import com.qrpay.app.R as resApp

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PersonalQRScreen(
    qrPayViewModel: QRPayViewModel,
    onBack: () -> Unit
) {
    val keyBoardController = LocalSoftwareKeyboardController.current
    val localFocus = LocalFocusManager.current
    val isCreatedQRCode by qrPayViewModel.isCreatedQRCode.collectAsState()

    val user by qrPayViewModel.userApp.collectAsState()


    var valueAmount by remember {
        mutableStateOf("")
    }

    var valueDescription by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                ImageView(it).apply {
                    Glide.with(it)
                        .load(resApp.drawable.background)
                        .centerCrop()
                        .into(this)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)),
        ) {
            HeaderApp(
                modifier = Modifier.fillMaxWidth(),
                content = "New QR",
                icon = resApp.drawable.ic_arrow_back
            ) {
                onBack()
            }

            Spacer(modifier = Modifier.padding(all = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)))

            user?.let {
                TextFieldWithOutBG(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    label = stringResource(id = resApp.string.phone_number),
                    placeholder = stringResource(id = resApp.string.enter_your_phone_number),
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                    keyboardAction = KeyboardActions(
                        onNext = { localFocus.moveFocus(FocusDirection.Next) }
                    ),
                    maxLength = 15,
                    enabled = false,
                    value = it.numberPhone,
                    onValueChange = {}
                )
            }

            Spacer(modifier = Modifier.padding(all = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)))

            TextFieldWithOutBG(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                label = stringResource(id = resApp.string.amount_money),
                placeholder = stringResource(id = resApp.string.enter_your_amount_money),
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                onValueChange = {
                    if (it.isNotEmpty()) {
                        valueAmount = it
                    }
                },
                maxLength = 50,
                keyboardAction = KeyboardActions(
                    onNext = { localFocus.moveFocus(FocusDirection.Next) }
                )
            )

            Spacer(modifier = Modifier.padding(all = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)))

            TextFieldWithOutBG(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                label = stringResource(id = resApp.string.description),
                placeholder = stringResource(id = resApp.string.enter_your_description),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                onValueChange = {
                    if (it.isNotEmpty()) {
                        valueDescription = it
                    }
                },
                maxLength = 20,
                keyboardAction = KeyboardActions(
                    onDone = { keyBoardController?.hide() }
                )
            )

            Spacer(modifier = Modifier.padding(all = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
            ) {
                Spacer(modifier = Modifier.weight(6f))

                TextButtonCorner(
                    modifier = Modifier.weight(4f),
                    textContent = "Create QR code"
                ) {
                    keyBoardController?.hide()
                    qrPayViewModel.updateStatusCreatedQR(true)
                    qrPayViewModel.createQRCode(
                        QRCodeContent(
                            numberPhone = user!!.numberPhone,
                            amount = valueAmount,
                            description = valueDescription
                        )
                    )
                }
            }
        }

        if (isCreatedQRCode) {
            ContentOfQRCode(
                modifier = Modifier.fillMaxSize(),
                qrPayViewModel = qrPayViewModel
            )
        }
    }
}

