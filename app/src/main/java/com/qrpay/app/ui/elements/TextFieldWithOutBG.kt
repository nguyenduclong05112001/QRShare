package com.qrpay.app.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.qrpay.app.core.GetDimension
import com.qrpay.app.ui.theme.*
import com.longhrk.dimension.R as resDimension

@Composable
fun TextFieldWithOutBG(
    modifier: Modifier,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    keyboardAction: KeyboardActions,
    maxLength: Int,
    enabled: Boolean = true,
    value: String = "",
    isError: Boolean = false,
    onValueChange: (String) -> Unit
) {
    var valueTextField by remember {
        mutableStateOf(value)
    }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            singleLine = true,
            textStyle = textFieldStyle.copy(fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp)),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = white,

                disabledBorderColor = gray,
                disabledTextColor = gray,
                disabledPlaceholderColor = gray,

                focusedBorderColor = white,
                unfocusedBorderColor = gray,

                errorBorderColor = red,
                errorLabelColor = red,
                errorCursorColor = red,
            ),
            enabled = enabled,
            label = {
                Text(
                    text = label,
                    style = textFieldStyle.copy(fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp))
                )
            },
            placeholder = {
                Text(
                    text = placeholder,
                    style = placeholderStyle.copy(fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp))
                )
            },
            value = if (!isError) valueTextField else "",
            onValueChange = {
                if (it.length <= maxLength) {
                    valueTextField = it
                }
                onValueChange(it)
            },
            keyboardActions = keyboardAction
        )
    }
}