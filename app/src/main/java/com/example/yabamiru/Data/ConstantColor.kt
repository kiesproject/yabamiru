package com.example.yabamiru.data

import android.util.Log
import com.example.yabamiru.R
import java.lang.IllegalArgumentException

object ConstantColor {

    var nowColorSet: MustColorSet = ColorSet0_20
        private set

    fun changeColorSet(id: Int) {
        Log.d("change color","set $id colorSet")
        nowColorSet = when (id) {
            0 -> ColorSet0_20
            1 -> ColorSet20_40
            2 -> ColorSet40_60
            3 -> ColorSet60_80
            4 -> ColorSet80_100
            else ->
                throw IllegalArgumentException("")
        }
    }
}

abstract class MustColorSet {
    abstract val colorPrimary: Int
    abstract val colorWindowBackground: Int
    abstract val colorPrimaryCardBackground: Int
    abstract val colorCardBackground: Int
    abstract val colorTagBackground: Int
    abstract val colorTagDoneBackground:Int
    abstract val colorFabBackground: Int
    abstract val colorText: Int
    abstract val colorTitleText: Int
    abstract val colorDokuro:Int
}

object ColorSet0_20 : MustColorSet() {
    override val colorPrimary: Int
        get() = R.color.colorPrimary0_20
    override val colorWindowBackground: Int
        get() = R.color.colorWindowBackground0_20
    override val colorPrimaryCardBackground: Int
        get() = R.color.colorPrimaryCardBackground0_20
    override val colorCardBackground: Int
        get() = R.color.colorCardBackground0_20
    override val colorTagBackground: Int
        get() = R.color.colorTagBackground0_20
    override val colorTagDoneBackground:Int
        get()= R.color.colorTagDoneBackground0_20
    override val colorFabBackground: Int
        get() = R.color.colorFabBackground0_20
    override val colorText: Int
        get() = R.color.colorText0_20
    override val colorTitleText: Int
        get() = R.color.colorTitleText0_20
    override val colorDokuro: Int
        get() = R.color.colorDokuro0_20
}

object ColorSet20_40 : MustColorSet() {
    override val colorPrimary: Int
        get() = R.color.colorPrimary20_40
    override val colorWindowBackground: Int
        get() = R.color.colorWindowBackground20_40
    override val colorPrimaryCardBackground: Int
        get() = R.color.colorPrimaryCardBackground20_40
    override val colorCardBackground: Int
        get() = R.color.colorCardBackground20_40
    override val colorTagBackground: Int
        get() = R.color.colorTagBackground20_40
    override val colorTagDoneBackground:Int
        get()= R.color.colorTagDoneBackground20_40
    override val colorFabBackground: Int
        get() = R.color.colorFabBackground20_40
    override val colorText: Int
        get() = R.color.colorText20_40
    override val colorTitleText: Int
        get() = R.color.colorTitleText20_40
    override val colorDokuro: Int
        get() = R.color.colorDokuro20_40
}

object ColorSet40_60 : MustColorSet() {
    override val colorPrimary: Int
        get() = R.color.colorPrimary40_60
    override val colorWindowBackground: Int
        get() = R.color.colorWindowBackground40_60
    override val colorPrimaryCardBackground: Int
        get() = R.color.colorPrimaryCardBackground40_60
    override val colorCardBackground: Int
        get() = R.color.colorCardBackground40_60
    override val colorTagBackground: Int
        get() = R.color.colorTagBackground40_60
    override val colorTagDoneBackground:Int
        get()= R.color.colorTagDoneBackground40_60
    override val colorFabBackground: Int
        get() = R.color.colorFabBackground40_60
    override val colorText: Int
        get() = R.color.colorText40_60
    override val colorTitleText: Int
        get() = R.color.colorTitleText40_60
    override val colorDokuro: Int
        get() = R.color.colorDokuro40_60
}

object ColorSet60_80 : MustColorSet() {
    override val colorPrimary: Int
        get() = R.color.colorPrimary60_80
    override val colorWindowBackground: Int
        get() = R.color.colorWindowBackground60_80
    override val colorPrimaryCardBackground: Int
        get() = R.color.colorPrimaryCardBackground60_80
    override val colorCardBackground: Int
        get() = R.color.colorCardBackground60_80
    override val colorTagBackground: Int
        get() = R.color.colorTagBackground60_80
    override val colorTagDoneBackground:Int
        get()= R.color.colorTagDoneBackground60_80
    override val colorFabBackground: Int
        get() = R.color.colorFabBackground60_80
    override val colorText: Int
        get() = R.color.colorText60_80
    override val colorTitleText: Int
        get() = R.color.colorTitleText60_80
    override val colorDokuro: Int
        get() = R.color.colorDokuro60_80
}

object ColorSet80_100 : MustColorSet() {
    override val colorPrimary: Int
        get() = R.color.colorPrimary80_100
    override val colorWindowBackground: Int
        get() = R.color.colorWindowBackground80_100
    override val colorPrimaryCardBackground: Int
        get() = R.color.colorPrimaryCardBackground80_100
    override val colorCardBackground: Int
        get() = R.color.colorCardBackground80_100
    override val colorTagBackground: Int
        get() = R.color.colorTagBackground80_100
    override val colorTagDoneBackground:Int
        get()= R.color.colorTagDoneBackground80_100
    override val colorFabBackground: Int
        get() = R.color.colorFabBackground80_100
    override val colorText: Int
        get() = R.color.colorText80_100
    override val colorTitleText: Int
        get() = R.color.colorTitleText80_100
    override val colorDokuro: Int
        get() = R.color.colorDokuro80_100
}