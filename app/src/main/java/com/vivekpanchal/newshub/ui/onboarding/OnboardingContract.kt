package com.vivekpanchal.newshub.ui.onboarding

import com.vivekpanchal.newshub.util.mvi.UiEffect
import com.vivekpanchal.newshub.util.mvi.UiIntent
import com.vivekpanchal.newshub.util.mvi.UiState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

// PersistentList (not the plain ImmutableList supertype) so toggleChoice() can use its add()/
// remove() methods, which return a new PersistentList without needing a full copy each time.
data class OnboardingState(
    val selectedChoices: PersistentList<String> = persistentListOf(),
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
