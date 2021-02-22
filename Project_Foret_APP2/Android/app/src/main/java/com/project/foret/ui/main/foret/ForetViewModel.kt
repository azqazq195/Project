package com.project.foret.ui.main.foret

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.model.Foret
import com.project.foret.repository.ForetRepository
import com.project.foret.response.BoardListResponse
import com.project.foret.response.UploadResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ForetViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val foret: MutableLiveData<Resource<Foret>> = MutableLiveData()
    val signUp: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()
    val noticeBoardList: MutableLiveData<Resource<BoardListResponse>> = MutableLiveData()
    val feedBoardList: MutableLiveData<Resource<BoardListResponse>> = MutableLiveData()

    fun signUp(foret_id: Long, member_id: Long) = viewModelScope.launch {
        signUp.postValue(Resource.Loading())
        val response = foretRepository.signUpForet(foret_id, member_id)
        signUp.postValue(handleSignUpResponse(response))
    }

    private fun handleSignUpResponse(response: Response<UploadResponse>) : Resource<UploadResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getForetDetails(foret_id: Long) = viewModelScope.launch {
        foret.postValue(Resource.Loading())
        val response = foretRepository.getForetDetails(foret_id)
        foret.postValue(handleForetResponse(response))
    }

    private fun handleForetResponse(response: Response<Foret>) : Resource<Foret>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getBoardList(foret_id: Long, type: Int) = viewModelScope.launch {
        when(type){
            1 -> {
                noticeBoardList.postValue(Resource.Loading())
                val response = foretRepository.getForetBoardList(foret_id, type)
                noticeBoardList.postValue(handleBoardListResponse(response))
            }
            3 -> {
                feedBoardList.postValue(Resource.Loading())
                val response = foretRepository.getForetBoardList(foret_id, type)
                feedBoardList.postValue(handleBoardListResponse(response))
            }
        }

    }

    private fun handleBoardListResponse(response: Response<BoardListResponse>) : Resource<BoardListResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}