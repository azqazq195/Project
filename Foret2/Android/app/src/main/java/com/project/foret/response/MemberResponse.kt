package com.project.foret.response

import com.project.foret.model.Member

data class MemberResponse(
    val member: Member,
    val message: String
)
