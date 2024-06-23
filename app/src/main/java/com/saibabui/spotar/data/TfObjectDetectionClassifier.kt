package com.saibabui.spotar.data

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import com.saibabui.spotar.domain.Classification
import com.saibabui.spotar.domain.ImageDetection
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions.Orientation
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class TfObjectDetectionClassifier(
    private val context: Context,
    private val threshold: Float,
    private val maxResults: Int,
) : ImageDetection {


    private var objectDetector: ObjectDetector? = null

    fun setupObjectDetection() {
        val baseOptions = BaseOptions.builder().setNumThreads(4).build()

        val options = ObjectDetector.ObjectDetectorOptions.builder().setBaseOptions(baseOptions)
            .setMaxResults(maxResults).setScoreThreshold(threshold).build()

        try {
            objectDetector =
                ObjectDetector.createFromFileAndOptions(context, "ob_detection.tflite", options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun detect(bitmap: Bitmap): List<Classification> {
        if (objectDetector == null) {
            setupObjectDetection()
            return emptyList()
        } else {
            val imageProcessor = ImageProcessor.Builder().build()

            val inputImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

            val imageProcessingOptions =
                ImageProcessingOptions.builder()
                    .build()


            val results = objectDetector?.detect(inputImage, imageProcessingOptions)
            return results?.flatMap { classification ->
                classification.categories.map {
                    Classification(it.label, it.score)
                }
            }?.distinctBy {
                it.label
            } ?: emptyList()
        }
    }

    private fun getOrientation(rotationDegrees: Int): Orientation {
        return when (rotationDegrees) {
            Surface.ROTATION_0 -> Orientation.RIGHT_TOP
            Surface.ROTATION_90 -> Orientation.TOP_LEFT
            Surface.ROTATION_180 -> Orientation.RIGHT_BOTTOM
            else -> Orientation.BOTTOM_RIGHT
        }
    }

}