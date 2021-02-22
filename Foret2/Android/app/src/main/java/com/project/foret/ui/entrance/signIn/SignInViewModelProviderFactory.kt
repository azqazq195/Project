package com.project.foret.ui.entrance.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.entrance.signUp.SignUpViewModel

class SignInViewModelProviderFactory(
    private val foretRepository: ForetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignInViewModel(foretRepository) as T
    }
}