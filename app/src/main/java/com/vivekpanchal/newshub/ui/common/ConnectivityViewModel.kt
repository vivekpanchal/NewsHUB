package com.vivekpanchal.newshub.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivekpanchal.newshub.data.connectivity.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Replaces the legacy `NetworkChangeReceiver` + bottom-sheet-dialog pattern with observable state. */
@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = connectivityObserver.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
}
