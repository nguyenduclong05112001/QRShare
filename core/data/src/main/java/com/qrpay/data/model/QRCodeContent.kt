package com.qrpay.data.model

data class QRCodeContent(
    val numberPhone: String,
    val amount: String = "",
    val description: String? = null
)