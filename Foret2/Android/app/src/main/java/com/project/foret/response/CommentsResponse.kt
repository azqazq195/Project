package com.project.foret.response

import com.project.foret.model.Comment

data class CommentsResponse(
    val total: Int,
    val comments: MutableList<Comment>
)