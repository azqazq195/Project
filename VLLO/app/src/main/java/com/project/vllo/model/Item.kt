package com.project.vllo.model

import android.net.Uri

data class Item(
    val uri: Uri,
    val name: String,
    val duration: Int?,
    val size: Int,
    val mimeType: String
)
