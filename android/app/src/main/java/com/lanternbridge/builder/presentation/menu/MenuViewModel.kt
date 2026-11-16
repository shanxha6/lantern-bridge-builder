package com.lanternbridge.builder.presentation.menu

import androidx.lifecycle.ViewModel
import com.lanternbridge.builder.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel(private val progress: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val selected = progress.selectedDistrict()
        _state.update {
            it.copy(
                lanterns = progress.lanternsLit(),
                stars = progress.totalStars(),
                districts = progress.districts(),
                selectedDistrict = selected,
                nextLevel = progress.nextLevel(selected)
            )
        }
    }

    fun selectDistrict(districtId: Int) {
        progress.selectDistrict(districtId)
        refresh()
    }
}
