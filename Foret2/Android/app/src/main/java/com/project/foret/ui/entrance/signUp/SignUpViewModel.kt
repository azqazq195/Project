package com.project.foret.ui.entrance.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.repository.ForetRepository
import com.project.foret.response.DefaultResponse
import com.project.foret.response.UploadResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class SignUpViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val checkEmail: MutableLiveData<Resource<DefaultResponse>> = MutableLiveData()
    val createMember: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()

    fun createMember(files: MultipartBody.Part, member: RequestBody) = viewModelScope.launch {
        createMember.postValue(Resource.Loading())
        val response = foretRepository.createMember(files, member)
        createMember.postValue(handleCreateMemberResponse(response))
    }

    private fun handleCreateMemberResponse(response: Response<UploadResponse>) : Resource<UploadResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun checkEmail(email: String) = viewModelScope.launch {
        checkEmail.postValue(Resource.Loading())
        val response = foretRepository.checkEmail(email)
        checkEmail.postValue(handleCheckEmailResponse(response))
    }

    private fun handleCheckEmailResponse(response: Response<DefaultResponse>) : Resource<DefaultResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}