package com.qrpay.app.ui

import com.qrpay.app.ui.event.NavEvent
import com.qrpay.app.ui.viewmodel.NavViewModel

class EventHandler(
    private val navigationViewModel: NavViewModel
) {
    fun postNavEvent(event: NavEvent){
        navigationViewModel.updateEvent(event)
    }
    fun navEvent() = navigationViewModel.event

    override fun toString(): String {
        return navEvent().value.toString()
    }
}