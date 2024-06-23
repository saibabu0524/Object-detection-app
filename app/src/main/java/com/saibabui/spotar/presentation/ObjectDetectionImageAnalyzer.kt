package com.saibabui.spotar.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.saibabui.spotar.domain.Classification
import com.saibabui.spotar.domain.ImageDetection

class ObjectDetectionImageAnalyzer(
    private val classifier: ImageDetection,
    private val onResults: (List<Classification>) -> Unit
) : ImageAnalysis.Analyzer {



    override fun analyze(image: ImageProxy) {
        val imageRotationDegrees = image.imageInfo.rotationDegrees
        val bitmap = image.toBitmap()
        val results = classifier.detect(bitmap)
        onResults(results)
        image.close()
    }
}