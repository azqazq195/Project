package com.project.foret.model

data class Comment(
    val id: Long?,
    val group_id: Long?,
    val content: String?,
    val reg_date: String?,
    val member: Member?,
    val board: Board?
) {
    constructor(group_id: Long?, content: String, member: Member, board: Board)
            : this(null, group_id, content, null, member, board)
}
