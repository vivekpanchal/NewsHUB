package com.vivekpanchal.newshub.ui.onboarding

import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.data.repository.UserPreferencesRepository
import com.vivekpanchal.newshub.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : MviViewModel<OnboardingState, OnboardingIntent, OnboardingEffect>(OnboardingState()) {

    override suspend fun handleIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.ToggleChoice -> toggleChoice(intent.category)
            is OnboardingIntent.ConfirmChoices -> confirmChoices()
        }
    }

    private fun toggleChoice(category: String) {
        val current = currentState.selectedChoices
        when {
            current.contains(category) -> setState { copy(selectedChoices = current - category) }
            current.size < OnboardingState.REQUIRED_CHOICES ->
                setState { copy(selectedChoices = current + category) }
            else -> setEffect { OnboardingEffect.ShowMessage(R.string.userIntrestError) }
        }
    }

    private suspend fun confirmChoices() {
        if (!currentState.canProceed) {
            setEffect { OnboardingEffect.ShowMessage(R.string.enter_Intrests) }
            return
        }
        userPreferencesRepository.setUserInterests(currentState.selectedChoices)
        userPreferencesRepository.setFirstLaunchDone()
        setEffect { OnboardingEffect.NavigateToMain }
    }
}
