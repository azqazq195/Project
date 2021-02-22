package com.project.flo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.flo.repository.SongRepository

class MainViewModelProviderFactory(
    private val songRepository: SongRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(songRepository) as T
    }
}