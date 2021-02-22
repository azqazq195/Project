package com.project.foret.ui.main.annoymousForum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.foret.repository.ForetRepository

class AnonymousForumViewModelProviderFactory(
    private val foretRepository: ForetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AnonymousForumViewModel(foretRepository) as T
    }
}