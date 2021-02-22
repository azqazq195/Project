package com.project.foret.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.model.Member
import com.project.foret.repository.ForetRepository
import com.project.foret.response.MemberResponse
import com.project.foret.response.SignInResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val member: MutableLiveData<Resource<Member>> = MutableLiveData()

    fun getMember(member_id: Long) = viewModelScope.launch {
        member.postValue(Resource.Loading())
        val response = foretRepository.getMember(member_id)
        member.postValue(handleMemberResponse(response))
    }

    private fun handleMemberResponse(response: Response<Member>) : Resource<Member> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}