package com.lanternbridge.builder.presentation.gallery

import androidx.lifecycle.ViewModel
import com.lanternbridge.builder.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GalleryViewModel(private val progress: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(GalleryUiState())
    val state: StateFlow<GalleryUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val designs = progress.designs()
        _state.update {
            it.copy(
                designs = designs,
                unlockedCount = designs.count { design -> design.unlocked }
            )
        }
    }
}
