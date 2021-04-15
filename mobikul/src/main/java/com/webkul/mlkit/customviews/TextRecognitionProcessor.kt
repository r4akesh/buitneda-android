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
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.webkul.mlkit.activities.CameraSearchActivity

import java.io.IOException

/**
 * Processor for the text recognition demo.
 */
class TextRecognitionProcessor(private val activityInstance: CameraSearchActivity) : VisionProcessorBase<FirebaseVisionText>() {

    private val detector: FirebaseVisionTextRecognizer

    init {
        detector = FirebaseVision.getInstance().onDeviceTextRecognizer
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: $e")
        }

    }

    override fun detectInImage(image: FirebaseVisionImage): Task<FirebaseVisionText> {
        return detector.processImage(image)
    }

    override fun onSuccess(
            results: FirebaseVisionText,
            frameMetadata: FrameMetadata,
            graphicOverlay: GraphicOverlay) {
        graphicOverlay.clear()
        //        List<FirebaseVisionText.Block> blocks = results.getBlocks();
        //        for (int i = 0; i < blocks.size(); i++) {
        //            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
        //            for (int j = 0; j < lines.size(); j++) {
        //                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
        //                for (int k = 0; k < elements.size(); k++) {
        //                    GraphicOverlay.Graphic textGraphic = new TextGraphic(graphicOverlay, elements.get(k));
        //                    graphicOverlay.add(textGraphic);
        //
        //                }
        //            }
        //        }


        activityInstance.updateSpinnerFromTextResults(results)
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Text detection failed.$e")
    }

    companion object {

        private val TAG = "TextRecognitionProcessr"
    }
}
