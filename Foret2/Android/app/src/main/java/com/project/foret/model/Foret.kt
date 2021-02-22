package com.project.foret.model

import java.util.*

data class Foret (
    val id: Long?,
    val leader: Member?,
    val name: String?,
    val introduce: String?,
    val max_member: Int?,
    val reg_date: Date?,
    val photos: MutableList<Photo>?,
    val tags: MutableList<Tag>?,
    val regions: MutableList<Region>?,
    val members: MutableList<Member>?
) {
    constructor(id: Long) :
            this(id, null, null,null, null, null, null, null, null, null)
    constructor(leader: Member, name: String, introduce: String, max_member: Int, tags: MutableList<Tag>, regions: MutableList<Region>) :
            this(null, leader, name, introduce, max_member, null, null, tags, regions, null)
}