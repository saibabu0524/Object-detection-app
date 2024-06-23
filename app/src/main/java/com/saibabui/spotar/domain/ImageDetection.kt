package com.saibabui.spotar.domain

import android.graphics.Bitmap

interface ImageDetection {
    fun detect(bitmap: Bitmap): List<Classification>
}