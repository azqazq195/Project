package com.project.foret.model

data class Board(
    val id: Long?,
    val type: Int?,
    val hit: Int?,
    val subject: String?,
    val content: String?,
    val reg_date: String?,
    val edit_date: String?,
    val member: Member?,
    val foret: Foret?,
    val photos: MutableList<Photo>?,
    val comments: MutableList<Comment>?,
    val likes: MutableList<Member>?,
    val comment_count: Int?,
    val like_count: Int?
) {
    constructor(id: Long)
            : this(id, null, null, null, null, null, null, null, null, null, null, null, null, null)
    constructor(type: Int, subject: String, content: String, member: Member, foret: Foret?)
            : this(null,type, null, subject, content, null, null, member, foret, null,  null,null, null, null)
}
