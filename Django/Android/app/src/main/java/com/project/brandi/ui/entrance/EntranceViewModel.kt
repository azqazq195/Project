package com.project.brandi.ui.entrance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.brandi.data.user.CreateResponse
import com.project.brandi.data.user.LoginResponse
import com.project.brandi.data.user.UserRepository
import com.project.brandi.util.Resource
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response

class EntranceViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val createUser: MutableLiveData<Resource<CreateResponse>> = MutableLiveData()
    val loginUser: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    fun createUser(user: RequestBody) = viewModelScope.launch {
        createUser.postValue(Resource.Loading())
        val response = userRepository.createUser(user)
        createUser.postValue(handleCreateUserResponse(response))
    }

    private fun handleCreateUserResponse(response: Response<CreateResponse>) : Resource<CreateResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun loginUser(user: RequestBody) = viewModelScope.launch {
        loginUser.postValue(Resource.Loading())
        val response = userRepository.loginUser(user)
        loginUser.postValue(handleLoginUserResponse(response))
    }

    private fun handleLoginUserResponse(response: Response<LoginResponse>) : Resource<LoginResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}