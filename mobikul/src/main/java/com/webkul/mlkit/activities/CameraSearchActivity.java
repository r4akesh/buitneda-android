package com.webkul.mlkit.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.webkul.mlkit.adapters.CameraSearchResultAdapter;
import com.webkul.mlkit.customviews.CameraSource;
import com.webkul.mlkit.customviews.CameraSourcePreview;
import com.webkul.mlkit.customviews.GraphicOverlay;
import com.webkul.mlkit.customviews.ImageLabelingProcessor;
import com.webkul.mlkit.customviews.TextRecognitionProcessor;
import com.webkul.mobikul.R;
import com.webkul.mobikul.helpers.BundleKeysHelper;
import com.webkul.mobikul.helpers.ToastHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.webkul.mobikul.helpers.BundleKeysHelper.CAMERA_SELECTED_MODEL;

/**
 * Webkul Software.
 *
 * @author Webkul
 * @Mobikul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 */

public class CameraSearchActivity extends AppCompatActivity {

    public static final String TAG = "CameraSearchActivity";

    public static final String TEXT_DETECTION = "Text Detection";
    public static final String IMAGE_LABEL_DETECTION = "Product Detection";
    private static final int PERMISSION_REQUESTS = 1;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private CameraSource cameraSource = null;
    private RecyclerView resultSpinner;
    private List<FirebaseVisionImageLabel> resultList;
    private List<String> displayList;
    private CameraSearchResultAdapter displayAdapter;
    private TextView resultNumberTv;
    private LinearLayout resultContainer;
    private boolean hasFlash;
    private String selectedModel = IMAGE_LABEL_DETECTION;

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "CameraSearchActivity isPermissionGranted Permission granted : " + permission);
            return true;
        }
        Log.d(TAG, "CameraSearchActivity isPermissionGranted: Permission NOT granted -->" + permission);
        return false;
    }

    @Override
    public void onBackPressed() {
        CameraSearchActivity.this.setResult(RESULT_CANCELED);
        CameraSearchActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_search);

        if (getIntent().hasExtra(CAMERA_SELECTED_MODEL)) {
            selectedModel = getIntent().getStringExtra(CAMERA_SELECTED_MODEL);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(selectedModel);
        }

        resultList = new ArrayList<>();
        displayList = new ArrayList<>();
        resultSpinner = (RecyclerView) findViewById(R.id.results_spinner);
        resultSpinner.setLayoutManager(new LinearLayoutManager(CameraSearchActivity.this, LinearLayoutManager.VERTICAL, false));
        displayAdapter = new CameraSearchResultAdapter(CameraSearchActivity.this, displayList);
        resultSpinner.setAdapter(displayAdapter);
//        resultContainer = (LinearLayout) findViewById(R.id.resultsContainer);
//        resultContainer.getLayoutParams().height = (int) (Utils.getScreenHeight() * 0.65);
        resultNumberTv = (TextView) findViewById(R.id.resultsMessageTv);
        resultNumberTv.setText(getString(R.string.x_results_found, displayList.size()));
        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "CameraSearchActivity onCreate Preview is null ");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "CameraSearchActivity onCreate graphicOverlay is null ");

        }

        ToggleButton facingSwitch = (ToggleButton) findViewById(R.id.facingswitch);
        if (Camera.getNumberOfCameras() == 2) {
            facingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cameraSource != null) {
                        if (isChecked) {
                            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
                        } else {
                            cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
                        }
                    }
                    preview.stop();
                    displayList.clear();
                    displayAdapter.notifyDataSetChanged();
                    startCameraSource();
                }
            });
        } else {
            facingSwitch.setEnabled(false);
            facingSwitch.setChecked(false);
            facingSwitch.setVisibility(View.GONE);
        }

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        ToggleButton flashButton = (ToggleButton) findViewById(R.id.flashSwitch);

        if (hasFlash) {
            flashButton.setVisibility(View.VISIBLE);
            flashButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cameraSource != null) {
                        Camera camera = cameraSource.getCamera();
                        Camera.Parameters parameters = camera.getParameters();
                        if (isChecked) {
                            parameters.setFlashMode(parameters.FLASH_MODE_TORCH);
                        } else {
                            parameters.setFlashMode(parameters.FLASH_MODE_OFF);
                        }
                        camera.setParameters(parameters);
                    } else {
                        ToastHelper.Companion.showToast(CameraSearchActivity.this, getString(R.string.error_while_using_flash), Toast.LENGTH_LONG);
                    }
                }
            });
        } else {
            flashButton.setVisibility(View.GONE);
        }

        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
        } else {
            getRuntimePermissions();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "CameraSearchActivity onResume: ");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "CameraSearchActivity startCameraSource resume: Preview is null ");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "CameraSearchActivity startCameraSource resume: graphOverlay is null ");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.d(TAG, "CameraSearchActivity startCameraSource : Unable to start camera source." + e.getMessage());
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    private void createCameraSource(String selectedModel) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {
            switch (selectedModel) {
                case TEXT_DETECTION:
                    cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor(CameraSearchActivity.this));
                    break;
                case IMAGE_LABEL_DETECTION:
                default:
                    cameraSource.setMachineLearningFrameProcessor(new ImageLabelingProcessor(CameraSearchActivity.this));
                    break;
            }
        } catch (Exception e) {
            Log.d(TAG, "CameraSearchActivity createCameraSource can not create camera source: " + e.getCause());
            e.printStackTrace();
        }
    }

    private String[] getRequiredPermissions() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateSpinnerFromResults(List<FirebaseVisionImageLabel> labelList) {
        for (FirebaseVisionImageLabel visionLabel : labelList) {
            if (!displayList.contains(visionLabel.getText()) && displayList.size() <= 9) {
                displayList.add(visionLabel.getText());
                resultList.add(visionLabel);
            }
        }
        resultNumberTv.setText(getString(R.string.x_results_found, displayList.size()));
        displayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;

        }
        return true;

    }

    public void updateSpinnerFromTextResults(FirebaseVisionText textresults) {
        List<FirebaseVisionText.TextBlock> blocks = textresults.getTextBlocks();
        for (FirebaseVisionText.TextBlock eachBlock : blocks) {
            for (FirebaseVisionText.Line eachLine : eachBlock.getLines()) {
                for (FirebaseVisionText.Element eachElement : eachLine.getElements()) {
                    if (!displayList.contains(eachElement.getText()) && displayList.size() <= 9) {
                        displayList.add(eachElement.getText());
                    }
                }
            }
        }
        resultNumberTv.setText(getString(R.string.x_results_found, displayList.size()));
        displayAdapter.notifyDataSetChanged();
    }

    public void sendResultBack(int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(BundleKeysHelper.CAMERA_SEARCH_HELPER, displayList.get(position));
        CameraSearchActivity.this.setResult(RESULT_OK, resultIntent);
        CameraSearchActivity.this.finish();
    }

}
