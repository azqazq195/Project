package com.project.foret.api

import com.project.foret.model.Board
import com.project.foret.model.Comment
import com.project.foret.model.Foret
import com.project.foret.model.Member
import com.project.foret.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ForetAPI {
    @GET("/member/checkEmail")
    suspend fun checkEmail(
        @Query("email") email: String,
    ): Response<DefaultResponse>

    @POST("/foret/signUp")
    suspend fun signUpForet(
        @Query("foret_id") foret_id: Long,
        @Query("member_id") member_id: Long
    ): Response<UploadResponse>

    @GET("/foret/all")
    suspend fun getForets(
    ): Response<ForetResponse>

    @GET("/foret/search")
    suspend fun getSearchForets(
        @Query("name") name: String
    ): Response<ForetResponse>

    @GET("/foret/page")
    suspend fun getRankForetsByPage(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ForetResponse>

    @GET("/foret/myForets")
    suspend fun getMyForets(
        @Query("member_id") member_id: Long
    ): Response<ForetResponse>

    @GET("/board/getHomeBoardList")
    suspend fun getHomeBoardList(
        @Query("foret_id") foret_id: Long
    ): Response<BoardListResponse>

    @GET("/board/getForetBoardList")
    suspend fun getForetBoardList(
        @Query("foret_id") foret_id: Long,
        @Query("type") type: Int
    ): Response<BoardListResponse>

    @GET("/board/getAnonymousBoardList/{order}")
    suspend fun getAnonymousBoardList(
        @Path("order") order: Int
    ): Response<BoardListResponse>

    @GET("/board/details/{board_id}")
    suspend fun getBoardDetails(
        @Path("board_id") board_id: Long
    ): Response<Board>

    @GET("/foret/details/{foret_id}")
    suspend fun getForetDetails(
        @Path("foret_id") foret_id: Long
    ): Response<Foret>



    @Multipart
    @POST("/foret/create")
    fun createForet(
        @Part files: MultipartBody.Part,
        @Part("foret") foret: RequestBody
    ): Call<UploadResponse>

    @Multipart
    @POST("/board/create")
    fun createBoard(
        @Part files: List<MultipartBody.Part>?,
        @Part("board") board: RequestBody
    ): Call<UploadResponse>

    @Multipart
    @POST("/member/create")
    suspend fun createMember(
        @Part files: MultipartBody.Part,
        @Part("member") member: RequestBody
    ): Response<UploadResponse>

    @GET("/member/signIn")
    suspend fun signIn(
        @QueryMap member: HashMap<String, String>
    ): Response<SignInResponse>

    @GET("/member/details/{id}")
    suspend fun getMember(
        @Path("id") id: Long
    ): Response<Member>

    // 댓글
    @GET("/comment/getComments")
    suspend fun getComments(
        @Query("board_id") board_id: Long
    ): Response<CommentsResponse>

    @POST("/comment/create")
    suspend fun createComment(
        @Body comment: Comment
    ): Response<CreateResponse>

    @DELETE("/comment/delete/{comment_id}")
    suspend fun deleteComment(
        @Path("comment_id") comment_id: Long
    ): Response<DefaultResponse>
}