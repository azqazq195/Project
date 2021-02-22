package com.project.foret.ui.entrance.signIn

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.repository.ForetRepository
import com.project.foret.response.SignInResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SignInViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val signIn: MutableLiveData<Resource<SignInResponse>> = MutableLiveData()

    fun signIn(member: HashMap<String, String>) = viewModelScope.launch {
        signIn.postValue(Resource.Loading())
        val response = foretRepository.signIn(member)
        signIn.postValue(handleSignInResponse(response))
    }

    private fun handleSignInResponse(response: Response<SignInResponse>) : Resource<SignInResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}