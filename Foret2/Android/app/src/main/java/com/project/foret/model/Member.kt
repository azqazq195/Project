package com.project.foret.model

import java.util.*

data class Member(
    var id: Long?,
    val name: String?,
    val nickname: String?,
    val email: String?,
    val birth: String?,
    val password: String?,
    val reg_date: Date?,
    val tags: MutableList<Tag>?,
    val regions: MutableList<Region>?,
    val photos: MutableList<Photo>?
) {
    constructor(id: Long)
            : this(id, null, null, null, null, null, null, null, null, null)

    constructor(
        name: String,
        nickname: String,
        email: String,
        birth: String,
        password: String,
        tags: MutableList<Tag>,
        regions: MutableList<Region>,
    ) : this(null, name, nickname, email, birth, password, null, tags, regions, null)

    constructor(
        email:String,
        password: String
    ) : this(null, null, null, email, null, password, null, null,null,null)
}
