package com.project.foret.repository

import com.project.foret.api.RetrofitInstance
import com.project.foret.model.Comment
import com.project.foret.response.CreateResponse
import com.project.foret.response.DefaultResponse
import com.project.foret.response.SignInResponse
import com.project.foret.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class ForetRepository {
    suspend fun getForets() =
        RetrofitInstance.api.getForets()

    suspend fun checkEmail(email: String): Response<DefaultResponse> =
        RetrofitInstance.api.checkEmail(email)

    suspend fun signUpForet(foret_id: Long, member_id: Long): Response<UploadResponse> =
        RetrofitInstance.api.signUpForet(foret_id, member_id)

    suspend fun getSearchForets(name: String) =
        RetrofitInstance.api.getSearchForets(name)

    suspend fun getRankForetsByPage(page: Int, size: Int) =
        RetrofitInstance.api.getRankForetsByPage(page, size)

    suspend fun getMyForets(member_id: Long) =
        RetrofitInstance.api.getMyForets(member_id)

    suspend fun getHomeBoardList(foret_id: Long) =
        RetrofitInstance.api.getHomeBoardList(foret_id)

    suspend fun getForetBoardList(foret_id: Long, type: Int) =
        RetrofitInstance.api.getForetBoardList(foret_id, type)

    suspend fun getForetDetails(foret_id: Long) =
        RetrofitInstance.api.getForetDetails(foret_id)

    suspend fun getBoardDetails(board_id: Long) =
        RetrofitInstance.api.getBoardDetails(board_id)

    suspend fun getAnonymousBoardList(order: Int) =
        RetrofitInstance.api.getAnonymousBoardList(order)



    fun createForet(files: MultipartBody.Part, foret: RequestBody): Call<UploadResponse> =
        RetrofitInstance.api.createForet(files, foret)

    fun createBoard(files: List<MultipartBody.Part>?, board: RequestBody): Call<UploadResponse> =
        RetrofitInstance.api.createBoard(files, board)

    suspend fun createMember(files: MultipartBody.Part, member: RequestBody): Response<UploadResponse> =
        RetrofitInstance.api.createMember(files, member)

    suspend fun signIn(member: HashMap<String, String>): Response<SignInResponse> =
        RetrofitInstance.api.signIn(member)

    suspend fun getMember(member_id: Long) =
        RetrofitInstance.api.getMember(member_id)


    // 댓글
    suspend fun getComments(board_id: Long) =
        RetrofitInstance.api.getComments(board_id)

    suspend fun createComment(comment: Comment): Response<CreateResponse> =
        RetrofitInstance.api.createComment(comment)

    suspend fun deleteComment(comment_id: Long): Response<DefaultResponse> =
        RetrofitInstance.api.deleteComment(comment_id)
}