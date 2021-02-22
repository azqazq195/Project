package com.project.foret.ui.main.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foret.model.Board
import com.project.foret.model.Comment
import com.project.foret.repository.ForetRepository
import com.project.foret.response.CommentsResponse
import com.project.foret.response.CreateResponse
import com.project.foret.response.DefaultResponse
import com.project.foret.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class BoardViewModel(
    val foretRepository: ForetRepository
) : ViewModel() {

    val board: MutableLiveData<Resource<Board>> = MutableLiveData()

    val commentList: MutableLiveData<Resource<CommentsResponse>> = MutableLiveData()
    var commentsResponse: CommentsResponse? = null

    val createBoard: MutableLiveData<Resource<CreateResponse>> = MutableLiveData()
    val createComment: MutableLiveData<Resource<CreateResponse>> = MutableLiveData()
    val deleteComment: MutableLiveData<Resource<DefaultResponse>> = MutableLiveData()

    fun getBoardDetails(board_id: Long) = viewModelScope.launch {
        board.postValue(Resource.Loading())
        val response = foretRepository.getBoardDetails(board_id)
        board.postValue(handleBoardDetailsResponse(response))
    }

    private fun handleBoardDetailsResponse(response: Response<Board>): Resource<Board> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getComments(board_id: Long) = viewModelScope.launch {
        commentList.postValue(Resource.Loading())
        val response = foretRepository.getComments(board_id)
        commentList.postValue(handleCommentsResponse(response))
    }

    private fun handleCommentsResponse(response: Response<CommentsResponse>): Resource<CommentsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun createComment(comment: Comment) = viewModelScope.launch {
        createComment.postValue(Resource.Loading())
        val response = foretRepository.createComment(comment)
        createComment.postValue(handleCreateCommentResponse(response))
    }

    private fun handleCreateCommentResponse(response: Response<CreateResponse>): Resource<CreateResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun deleteComment(comment_id: Long) = viewModelScope.launch {
        deleteComment.postValue(Resource.Loading())
        val response = foretRepository.deleteComment(comment_id)
        deleteComment.postValue(handleDeleteCommentResponse(response))
    }

    private fun handleDeleteCommentResponse(response: Response<DefaultResponse>): Resource<DefaultResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}