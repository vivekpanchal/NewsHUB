package com.vivekpanchal.newshub.util.mvi

/** Marker for a screen's immutable, renderable state. */
interface UiState

/** Marker for a user action / event dispatched from the UI to the ViewModel. */
interface UiIntent

/** Marker for a one-shot side effect consumed by the UI (navigation, toast, etc). */
interface UiEffect
