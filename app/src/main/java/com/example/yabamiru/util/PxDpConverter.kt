package com.example.yabamiru.util

import android.content.Context

object PxDpConverter {
    fun dp2Px(dp:Float,context: Context):Float{
        val metrics = context.resources.displayMetrics
        return dp * metrics.density
    }

    fun px2Dp(px:Int,context: Context):Float{
        val metrics = context.resources.displayMetrics
        return px / metrics.density
    }
}