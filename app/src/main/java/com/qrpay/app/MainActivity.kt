package com.qrpay.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.qrpay.app.ui.EventHandler
import com.qrpay.app.ui.NavGraph
import com.qrpay.app.ui.extensions.handleNavEvent
import com.qrpay.app.ui.theme.QRPayTheme
import com.qrpay.app.ui.viewmodel.NavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navigationViewModel by viewModels<NavViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val eventHandler = remember {
                EventHandler(navigationViewModel)
            }
            QRPayTheme(darkTheme = true) {
                GraphMainApp(eventHandler)
            }
        }
    }
}

@Composable
private fun GraphMainApp(eventHandler: EventHandler) {
    val navController = rememberNavController()
    LaunchedEffect(Unit) {
        eventHandler.navEvent().collect {
            navController.handleNavEvent(it)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        NavGraph(eventHandler = eventHandler, navController = navController)
    }
}
