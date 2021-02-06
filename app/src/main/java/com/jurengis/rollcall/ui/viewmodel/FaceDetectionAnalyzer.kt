package com.jurengis.rollcall.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectionAnalyzer : ViewModel(), ImageAnalysis.Analyzer {

    val faceRecognized: MutableLiveData<Boolean> = MutableLiveData(false)

    // High-accuracy landmark detection and face classification
    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(options)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            detector.process(image)
                .addOnSuccessListener { faces ->
                    if (faces.size > 0 && faceRecognized.value != true) {
                        Log.d(TAG, "Face detected")
                        // TODO: Recognize detected face
                        faceRecognized.postValue(true)
                    }
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "Fail to detect face")
                    e.message?.let { Log.d(TAG, it) }
                }
                .addOnCompleteListener {
                    // avoid blocking the production of further images
                    mediaImage.close()
                    imageProxy.close()
                }
        }
    }

    companion object {
        private const val TAG = "FaceDetectionAnalyzer"
    }
}