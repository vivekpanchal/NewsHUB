package com.vivekpanchal.newshub.ui.splash

import com.vivekpanchal.newshub.data.repository.UserPreferencesRepository
import com.vivekpanchal.newshub.util.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : MviViewModel<SplashState, SplashIntent, SplashEffect>(SplashState()) {

    init {
        setIntent(SplashIntent.CheckFirstLaunch)
    }

    override suspend fun handleIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.CheckFirstLaunch -> checkFirstLaunch()
        }
    }

    private suspend fun checkFirstLaunch() {
        delay(SPLASH_DELAY_MS)
        val isFirstLaunch = userPreferencesRepository.isFirstLaunch.first()
        setState { copy(isLoading = false) }
        if (isFirstLaunch) {
            setEffect { SplashEffect.NavigateToOnboarding }
        } else {
            setEffect { SplashEffect.NavigateToMain }
        }
    }

    private companion object {
        const val SPLASH_DELAY_MS = 1000L
    }
}
