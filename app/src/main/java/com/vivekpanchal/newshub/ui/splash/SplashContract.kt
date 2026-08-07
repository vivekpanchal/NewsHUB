package com.vivekpanchal.newshub.ui.splash

import com.vivekpanchal.newshub.mvi.UiEffect
import com.vivekpanchal.newshub.mvi.UiIntent
import com.vivekpanchal.newshub.mvi.UiState

data class SplashState(
    val isLoading: Boolean = true,
) : UiState

sealed interface SplashIntent : UiIntent {
    data object CheckFirstLaunch : SplashIntent
}

sealed interface SplashEffect : UiEffect {
    data object NavigateToOnboarding : SplashEffect
    data object NavigateToMain : SplashEffect
}
