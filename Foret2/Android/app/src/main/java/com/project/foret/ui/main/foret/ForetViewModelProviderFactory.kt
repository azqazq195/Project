package com.project.foret.ui.main.foret

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.foret.repository.ForetRepository

class ForetViewModelProviderFactory(
    private val foretRepository: ForetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForetViewModel(foretRepository) as T
    }
}