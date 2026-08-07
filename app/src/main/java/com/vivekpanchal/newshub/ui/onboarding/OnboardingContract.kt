package com.vivekpanchal.newshub.ui.onboarding

import com.vivekpanchal.newshub.mvi.UiEffect
import com.vivekpanchal.newshub.mvi.UiIntent
import com.vivekpanchal.newshub.mvi.UiState

data class OnboardingState(
    val selectedChoices: List<String> = emptyList(),
) : UiState {
    val remainingChoices: Int get() = (REQUIRED_CHOICES - selectedChoices.size).coerceAtLeast(0)
    val canProceed: Boolean get() = selectedChoices.size == REQUIRED_CHOICES

    companion object {
        const val REQUIRED_CHOICES = 3
    }
}

sealed interface OnboardingIntent : UiIntent {
    data class ToggleChoice(val category: String) : OnboardingIntent
    data object ConfirmChoices : OnboardingIntent
}

sealed interface OnboardingEffect : UiEffect {
    data object NavigateToMain : OnboardingEffect
    data class ShowMessage(val messageResId: Int) : OnboardingEffect
}
