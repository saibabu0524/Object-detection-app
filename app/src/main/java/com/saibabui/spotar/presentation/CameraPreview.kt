package com.saibabui.spotar.presentation

import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(modifier: Modifier = Modifier, controller: LifecycleCameraController) {

    val lifecycleOwner = LocalLifecycleOwner.current


    AndroidView(modifier = modifier, factory = { context ->
        PreviewView(context).apply {
            this.controller = controller
            controller.bindToLifecycle(lifecycleOwner)
        }
    })
}


fun Bitmap.centerCrop(width: Int, height: Int): Bitmap {
    val xStart = (this.width - width) / 2
    val yStart = (this.height - height) / 2
    if (xStart < 0 || yStart < 0 || xStart + width > this.width || yStart + height > this.height){
        throw IllegalArgumentException("Crop rectangle does not fit within bitmap.")
    }

    return Bitmap.createBitmap(this, xStart, yStart, width, height)
}