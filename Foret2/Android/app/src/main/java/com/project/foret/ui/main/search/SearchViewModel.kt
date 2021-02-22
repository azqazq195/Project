package com.project.foret.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.repository.ForetRepository
import com.project.foret.response.ForetResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {
    val rankForets: MutableLiveData<Resource<ForetResponse>> = MutableLiveData()
    val searchForets: MutableLiveData<Resource<ForetResponse>> = MutableLiveData()

    fun getRankForetsByPage(page: Int, size: Int) = viewModelScope.launch {
        rankForets.postValue(Resource.Loading())
        val response = foretRepository.getRankForetsByPage(page, size)
        rankForets.postValue(handleRankForetResponse(response))
    }

    private fun handleRankForetResponse(response: Response<ForetResponse>) : Resource<ForetResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getSearchForets(name: String) = viewModelScope.launch {
        searchForets.postValue(Resource.Loading())
        val response = foretRepository.getSearchForets(name)
        searchForets.postValue(handleSearchForetResponse(response))
    }

    private fun handleSearchForetResponse(response: Response<ForetResponse>) : Resource<ForetResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}