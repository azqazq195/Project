package com.project.foret.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.repository.ForetRepository
import com.project.foret.response.BoardListResponse
import com.project.foret.response.ForetResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val forets: MutableLiveData<Resource<ForetResponse>> = MutableLiveData()
    val homeBoardList: MutableLiveData<Resource<BoardListResponse>> = MutableLiveData()

    fun getMyForets(member_id: Long) = viewModelScope.launch {
        forets.postValue(Resource.Loading())
        val response = foretRepository.getMyForets(member_id)
        forets.postValue(handleForetResponse(response))
    }

    private fun handleForetResponse(response: Response<ForetResponse>) : Resource<ForetResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getHomeBoardList(foret_id: Long) = viewModelScope.launch {
        homeBoardList.postValue(Resource.Loading())
        val response = foretRepository.getHomeBoardList(foret_id)
        homeBoardList.postValue(handleForetBoardListResponse(response))
    }

    private fun handleForetBoardListResponse(response: Response<BoardListResponse>) : Resource<BoardListResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}