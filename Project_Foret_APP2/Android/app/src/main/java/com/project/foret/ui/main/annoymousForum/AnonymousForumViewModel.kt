package com.project.foret.ui.main.annoymousForum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.repository.ForetRepository
import com.project.foret.response.BoardListResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AnonymousForumViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val anonymousBoardList: MutableLiveData<Resource<BoardListResponse>> = MutableLiveData()

    fun getAnonymousBoardList(order: Int) = viewModelScope.launch {
        anonymousBoardList.postValue(Resource.Loading())
        val response = foretRepository.getAnonymousBoardList(order)
        anonymousBoardList.postValue(handleAnonymousBoardListResponse(response))
    }

    private fun handleAnonymousBoardListResponse(response: Response<BoardListResponse>): Resource<BoardListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}