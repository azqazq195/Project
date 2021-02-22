package com.project.foret.model

import java.util.*

data class Photo(
    val id: Long,
    val dir: String,
    val filename: String,
    val originname: String,
    val filesize: Int,
    val filetype: String,
    val reg_date: Date
)
