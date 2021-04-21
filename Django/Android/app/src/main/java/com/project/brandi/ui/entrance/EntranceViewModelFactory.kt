package com.project.brandi.ui.entrance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.brandi.data.user.UserRepository

@Suppress("UNCHECKED_CAST")
class EntranceViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntranceViewModel(userRepository) as T
    }
}