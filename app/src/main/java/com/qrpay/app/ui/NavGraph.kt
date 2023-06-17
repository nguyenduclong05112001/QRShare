package com.qrpay.app.ui

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qrpay.app.ui.event.NavEvent
import com.qrpay.app.ui.screen.*
import com.qrpay.app.ui.viewmodel.QRPayViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavGraph(eventHandler: EventHandler, navController: NavHostController) {
    val startDestination = NavTarget.Splash.route

    val activityScope = LocalContext.current as ComponentActivity
    val qrPayViewModel = hiltViewModel<QRPayViewModel>(activityScope)

    NavHost(navController, startDestination) {
        composable(NavTarget.Splash.route) {
            SplashScreen {
                eventHandler.postNavEvent(
                    event = NavEvent.ActionWithPopUp(
                        target = NavTarget.Home,
                        popupTarget = NavTarget.Splash,
                        inclusive = true
                    )
                )
            }
        }

        composable(NavTarget.Home.route) {
            HomeScreen(
                qrPayViewModel = qrPayViewModel,
                onPersonalQRScreen = {
                    eventHandler.postNavEvent(
                        event = NavEvent.Action(
                            target = NavTarget.PersonalQR
                        )
                    )
                },
                onSpliceBillRScreen = {
                    eventHandler.postNavEvent(
                        event = NavEvent.Action(
                            target = NavTarget.SpliceBill
                        )
                    )
                },
            )
        }

        composable(NavTarget.PersonalQR.route) {
            PersonalQRScreen(
                qrPayViewModel = qrPayViewModel,
                onBack = {
                    eventHandler.postNavEvent(
                        event = NavEvent.PopBackStack()
                    )
                }
            )
        }

        composable(NavTarget.SpliceBill.route) {
            SpliceBillScreen(
                qrPayViewModel = qrPayViewModel,
                onBack = {
                    eventHandler.postNavEvent(
                        event = NavEvent.PopBackStack()
                    )
                }
            )
        }
    }
}
