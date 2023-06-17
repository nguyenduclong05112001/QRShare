package com.qrpay.app.ui.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.Glide
import com.qrpay.app.core.GetDimension
import com.qrpay.app.ui.elements.*
import com.qrpay.app.ui.theme.black
import com.qrpay.app.ui.theme.white
import com.qrpay.app.ui.viewmodel.QRPayViewModel
import com.qrpay.app.util.convert
import com.qrpay.data.database.entity.User
import com.qrpay.data.model.QRCodeContent
import jp.wasabeef.blurry.Blurry
import com.longhrk.dimension.R as resDimension
import com.qrpay.app.R as resApp


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(
    qrPayViewModel: QRPayViewModel,
    onPersonalQRScreen: () -> Unit,
    onSpliceBillRScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        qrPayViewModel.getUserInDB()
    }

    val isUpdatePhone by qrPayViewModel.isUpdatePhone.collectAsState()
    val isCreatedQRCode by qrPayViewModel.isCreatedQRCode.collectAsState()
    val isNumberAvailable by qrPayViewModel.isNumberPhoneAvailable.collectAsState()
    val user by qrPayViewModel.userApp.collectAsState() ?: return

    BackHandler {
        if (isUpdatePhone) {
            qrPayViewModel.showUpdatePhone(false)
        }
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
            })

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (header, contentOfUser, button) = createRefs()

            HeaderApp(
                modifier = Modifier
                    .constrainAs(header) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        height = Dimension.wrapContent
                        width = Dimension.matchParent

                    }
                    .padding(all = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)),
                icon = resApp.drawable.ic_menu
            ) {}

            user?.let {
                ContentOfUser(
                    user = it,
                    qrPayViewModel = qrPayViewModel,
                    modifier = Modifier
                        .padding(
                            vertical = GetDimension.dimensionOf(res = resDimension.dimen._30sdp)
                        )
                        .constrainAs(contentOfUser) {
                            top.linkTo(header.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                            height = Dimension.wrapContent
                            width = Dimension.matchParent
                        },
                )
            }

            ComponentButtons(
                modifier = Modifier
                    .padding(
                        vertical = GetDimension.dimensionOf(res = resDimension.dimen._30sdp),
                        horizontal = GetDimension.dimensionOf(res = resDimension.dimen._20sdp)
                    )
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(contentOfUser.bottom)

                        height = Dimension.fillToConstraints
                        width = Dimension.matchParent
                    },
                onPersonalQRScreen = onPersonalQRScreen,
                onSpliceBillRScreen = onSpliceBillRScreen
            )
        }

        if (!isNumberAvailable) {
            InputNumberPhone(
                modifier = Modifier
                    .fillMaxSize(),
                qrPayViewModel = qrPayViewModel
            )
        }

        if (isCreatedQRCode) {
            ContentOfQRCode(
                modifier = Modifier.fillMaxSize(),
                qrPayViewModel = qrPayViewModel
            )
        }

        if (isUpdatePhone) {
            UpdatePhone(
                modifier = Modifier
                    .fillMaxSize(),
                qrPayViewModel = qrPayViewModel
            )
        }
    }
}

@Composable
fun InputNumberPhone(
    modifier: Modifier,
    qrPayViewModel: QRPayViewModel
) {
    val view = LocalView.current

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                ImageView(context).apply {
                    Blurry.with(context).capture(view).into(this)
                    setColorFilter(Color.parseColor("#7F000000"))
                }
            }
        )

        ComposeInputPhone(
            qrPayViewModel = qrPayViewModel,
            modifier = Modifier
                .clip(RoundedCornerShape(15))
                .align(Alignment.Center)
                .padding(horizontal = GetDimension.dimensionOf(res = resDimension.dimen._20sdp))
                .fillMaxWidth()
                .background(black),
            content = "enter your phone number to start app",
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposeInputPhone(
    modifier: Modifier,
    content: String,
    qrPayViewModel: QRPayViewModel
) {
    val keyBoardController = LocalSoftwareKeyboardController.current

    val user by qrPayViewModel.userApp.collectAsState()

    var valueOfTextField by remember {
        mutableStateOf(user?.numberPhone ?: "")
    }

    var isError by remember {
        mutableStateOf(false)
    }

    ConstraintLayout(
        modifier = modifier.padding(
            horizontal = GetDimension.dimensionOf(res = resDimension.dimen._30sdp),
            vertical = GetDimension.dimensionOf(res = resDimension.dimen._20sdp)
        )
    ) {
        val (text, input, button) = createRefs()

        Box(modifier = Modifier
            .padding(
                vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)
            )
            .constrainAs(text) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)

                width = Dimension.matchParent
                height = Dimension.wrapContent
            }) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                text = content, style = TextStyle(
                    fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp),
                    color = white,
                    fontWeight = FontWeight.W800,
                    textAlign = TextAlign.Center
                )
            )
        }

        TextFieldWithOutBG(
            modifier = Modifier
                .padding(
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)
                )
                .constrainAs(input) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(text.bottom)
                    bottom.linkTo(button.top)

                    height = Dimension.wrapContent
                    width = Dimension.matchParent
                },
            label = stringResource(id = resApp.string.phone_number),
            placeholder = stringResource(id = resApp.string.enter_your_phone_number),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            onValueChange = {
                if (it.isNotEmpty()) {
                    valueOfTextField = it
                }
            },
            maxLength = 20,
            isError = isError,
            keyboardAction = KeyboardActions(
                onDone = { keyBoardController?.hide() }
            )
        )

        TextButtonCorner(
            modifier = Modifier
                .padding(
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)
                )
                .constrainAs(button) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.wrapContent
                    width = Dimension.ratio("2:1")
                },
            textContent = "Save"
        ) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(valueOfTextField)) {
                isError = false
                qrPayViewModel.setIsNumberPhoneAvailable(isStatus = true)
                qrPayViewModel.setUser(User(numberPhone = valueOfTextField))
                keyBoardController?.hide()
            } else {
                isError = true
            }
        }
    }
}

@Composable
fun UpdatePhone(
    modifier: Modifier,
    qrPayViewModel: QRPayViewModel
) {
    val view = LocalView.current

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier
                .clickable {
                    qrPayViewModel.showUpdatePhone(false)
                }
                .fillMaxSize(),
            factory = { context ->
                ImageView(context).apply {
                    Blurry.with(context).capture(view).into(this)
                    setColorFilter(Color.parseColor("#7F000000"))
                }
            }
        )

        ComposeUpdatePhone(
            qrPayViewModel = qrPayViewModel,
            modifier = Modifier
                .clip(RoundedCornerShape(15))
                .align(Alignment.Center)
                .padding(horizontal = GetDimension.dimensionOf(res = resDimension.dimen._20sdp))
                .fillMaxWidth()
                .background(black),
            content = "Update your phone",
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposeUpdatePhone(
    modifier: Modifier,
    content: String,
    qrPayViewModel: QRPayViewModel
) {
    val keyBoardController = LocalSoftwareKeyboardController.current

    val user = qrPayViewModel.userApp.collectAsState().value

    var valueOfTextField by remember {
        mutableStateOf(user!!.numberPhone)
    }

    var isError by remember {
        mutableStateOf(false)
    }

    ConstraintLayout(
        modifier = modifier.padding(
            horizontal = GetDimension.dimensionOf(res = resDimension.dimen._30sdp),
            vertical = GetDimension.dimensionOf(res = resDimension.dimen._20sdp)
        )
    ) {
        val (text, input, button) = createRefs()

        Box(modifier = Modifier
            .padding(
                vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)
            )
            .constrainAs(text) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)

                width = Dimension.matchParent
                height = Dimension.wrapContent
            }) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                text = content, style = TextStyle(
                    fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp),
                    color = white,
                    fontWeight = FontWeight.W800,
                    textAlign = TextAlign.Center
                )
            )
        }

        TextFieldWithOutBG(
            modifier = Modifier
                .padding(
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)
                )
                .constrainAs(input) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(text.bottom)
                    bottom.linkTo(button.top)

                    height = Dimension.wrapContent
                    width = Dimension.matchParent
                },
            label = stringResource(id = resApp.string.phone_number),
            placeholder = stringResource(id = resApp.string.enter_your_phone_number),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            value = valueOfTextField,
            onValueChange = {
                if (it.isNotEmpty()) {
                    valueOfTextField = it
                }
            },
            maxLength = 20,
            isError = isError,
            keyboardAction = KeyboardActions(
                onDone = { keyBoardController?.hide() }
            )
        )

        TextButtonCorner(
            modifier = Modifier
                .padding(
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)
                )
                .constrainAs(button) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.wrapContent
                    width = Dimension.ratio("2:1")
                },
            textContent = "Update"
        ) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(valueOfTextField)) {
                isError = false
                if (user != null) {
                    qrPayViewModel.updateUserInDB(user.copy(numberPhone = valueOfTextField))
                }
                keyBoardController?.hide()
                qrPayViewModel.showUpdatePhone(false)
            } else {
                isError = true
            }
        }
    }
}

@Composable
fun ComponentButtons(
    modifier: Modifier,
    onPersonalQRScreen: () -> Unit,
    onSpliceBillRScreen: () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "What is your option?", style = TextStyle(
                fontWeight = FontWeight.W600,
                color = white,
                textAlign = TextAlign.Center,
                fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp)
            )
        )

        Row(
            modifier = Modifier
                .padding(
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._15sdp)
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(GetDimension.dimensionOf(res = resDimension.dimen._15sdp))
        )
        {
            CTAButton(modifier = Modifier.weight(1f), content = "New QR") {
                onPersonalQRScreen()
            }

            CTAButton(modifier = Modifier.weight(1f), content = "Splice Bill") {
                onSpliceBillRScreen()
            }
        }
    }
}

@Composable
fun ContentOfUser(
    qrPayViewModel: QRPayViewModel,
    modifier: Modifier,
    user: User
) {
    val context = LocalContext.current

    val localDensity = LocalDensity.current
    var sizePhoto by remember {
        mutableStateOf(0.dp)
    }

    val bitmap by remember {
        mutableStateOf(
            user.avatar.convert()
        )
    }

    var imageData by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            imageData = it
        }

    Column(
        modifier = modifier.onGloballyPositioned {
            sizePhoto = with(localDensity) {
                (it.size.width.toDp())
            }
        },
        verticalArrangement = Arrangement.spacedBy(GetDimension.dimensionOf(res = resDimension.dimen._30sdp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageData != null) {
            val bitmapSelected = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images
                    .Media.getBitmap(context.contentResolver, imageData)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, imageData!!)
                ImageDecoder.decodeBitmap(source)
            }
            qrPayViewModel.updateUserInDB(user = user!!.copy(avatar = bitmapSelected.convert()))

            AndroidView(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable {
                        launcher.launch("image/*")
                    }
                    .size(sizePhoto / 2),
                factory = { context ->
                    ImageView(context).apply {
                        Glide.with(context)
                            .load(imageData)
                            .centerCrop()
                            .into(this@apply)
                    }
                }
            )
        } else {
            AndroidView(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable {
                        launcher.launch("image/*")
                    }
                    .size(sizePhoto / 2),
                factory = { context ->
                    ImageView(context).apply {
                        Glide.with(context)
                            .load(bitmap)
                            .error(resApp.drawable.avatar_empty)
                            .centerCrop()
                            .into(this@apply)
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .width(sizePhoto / 1.5f)
                .border(
                    width = GetDimension.dimensionOf(res = resDimension.dimen._1sdp),
                    color = white,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clickable {
                        qrPayViewModel.createQRCode(
                            QRCodeContent(
                                numberPhone = user!!.numberPhone,
                            )
                        )
                        qrPayViewModel.updateStatusCreatedQR(true)
                    }
                    .background(white)
                    .padding(all = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
            ) {
                Icon(
                    painter = painterResource(id = resApp.drawable.ic_qr_code),
                    tint = black,
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                    .clickable {
                        qrPayViewModel.showUpdatePhone(true)
                    }
                    .weight(1f)
                    .padding(
                        horizontal = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)
                    )
            ) {
                user?.let {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = it.numberPhone,
                        style = TextStyle(
                            fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp),
                            fontWeight = FontWeight.Bold,
                            color = white
                        )
                    )
                }
            }
        }
    }
}

fun convertResToBitmap(
    res: Int,
    context: Context
): Bitmap {
    val bitmap = BitmapFactory.decodeResource(context.resources, res).convert()
    return bitmap.convert()
}

