package com.vivekpanchal.newshub.util.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base class for MVI-style ViewModels.
 *
 * Unidirectional data flow: the UI dispatches [UiIntent]s via [setIntent], intents are
 * reduced into a single [state] ([StateFlow]), and one-shot, non-state side effects
 * (navigation, toasts, snackbars) are emitted through [effect].
 */
abstract class MviViewModel<S : UiState, I : UiIntent, E : UiEffect>(
    initialState: S,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()
    val currentState: S get() = _state.value

    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    private val _intent = MutableSharedFlow<I>()

    init {
        viewModelScope.launch {
            _intent.collect { intent -> handleIntent(intent) }
        }
    }

    fun setIntent(intent: I) {
        viewModelScope.launch { _intent.emit(intent) }
    }

    protected abstract suspend fun handleIntent(intent: I)

    protected fun setState(reducer: S.() -> S) {
        _state.update(reducer)
    }

    protected fun setEffect(builder: () -> E) {
        viewModelScope.launch { _effect.send(builder()) }
    }
}
