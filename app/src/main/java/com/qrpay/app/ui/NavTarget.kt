package com.qrpay.app.ui

sealed class NavTarget(val route: String) {

    object Splash : NavTarget("splash")

    object Home : NavTarget("home")

    object PersonalQR : NavTarget("personal_qr")

    object SpliceBill : NavTarget("splice_bill")

    override fun toString(): String {
        return route
    }
}