package io.intrepid.skotlinton.screens.example1

sealed class Example1ViewEvent {
    object GotoExample2 : Example1ViewEvent()
    data class ShowCurrentTimeToast(val message: String) : Example1ViewEvent()
}
