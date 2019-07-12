package io.intrepid.skotlinton.screens.example1

import io.intrepid.skotlinton.utils.ViewEvent

sealed class Example1ViewEvent : ViewEvent {
    object GotoExample2 : Example1ViewEvent()
    data class ShowCurrentTimeToast(val message: String) : Example1ViewEvent()
}
