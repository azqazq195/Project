package com.project.vllo.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar

fun View.snackBar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}

// 기기별 해상도 얻는 Util
fun ScreenSizeUtil(context: Context): DisplayMetrics {
    val metrics = DisplayMetrics()
    var windowManager: WindowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics
}