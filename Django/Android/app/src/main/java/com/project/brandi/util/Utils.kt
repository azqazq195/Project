package com.project.brandi.util


import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.snackbar.Snackbar
import com.project.brandi.R

fun View.snackBar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}

fun showLoadingDialog(context: Context): Dialog {
    val progressDialog = Dialog(context)

    progressDialog.let {
        it.show()
        it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
//        it.setContentView(R.layout.dialog_loading)
        it.setCancelable(false)
        it.setCanceledOnTouchOutside(false)
        return it
    }
}

