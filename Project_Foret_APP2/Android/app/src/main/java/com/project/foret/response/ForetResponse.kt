package com.project.foret.response

import com.project.foret.model.Foret

data class ForetResponse (
    val total: Int,
    val forets: MutableList<Foret>
)