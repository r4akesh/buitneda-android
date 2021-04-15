// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.webkul.mlkit.customviews

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions
import com.webkul.mlkit.activities.CameraSearchActivity
import java.io.IOException

/**
 * Image Label Demo.
 */
class ImageLabelingProcessor(private val activityInstance: CameraSearchActivity) : VisionProcessorBase<List<FirebaseVisionImageLabel>>() {

    private val detector: FirebaseVisionImageLabeler

    init {
        val options = FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.75f)
                .build()
        detector = FirebaseVision.getInstance().getOnDeviceImageLabeler(options)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: $e")
        }

    }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionImageLabel>> {
        return detector.processImage(image)
    }

    override fun onSuccess(
            labels: List<FirebaseVisionImageLabel>,
            frameMetadata: FrameMetadata,
            graphicOverlay: GraphicOverlay) {
        graphicOverlay.clear()
        activityInstance.updateSpinnerFromResults(labels)
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Label detection failed.$e")
    }

    companion object {

        private val TAG = "ImageLabelingProcessor"
    }
}
