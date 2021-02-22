package com.project.flo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.flo.model.Song
import com.project.flo.repository.SongRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    val songRepository: SongRepository
) : ViewModel() {

    val song: MutableLiveData<Song> = MutableLiveData()

    fun getSong() = viewModelScope.launch {
        val response = songRepository.getSong()
        song.postValue(handleMemberResponse(response))
    }

    private fun handleMemberResponse(response: Response<Song>) : Song? {
        if(response.isSuccessful){
            return response.body()
        }
        return null
    }
}