package com.kumar.akshay.libmag.Barcode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.kumar.akshay.libmag.R;
import com.kumar.akshay.libmag.Student.BookAvailability;
import com.kumar.akshay.libmag.librarian.Fine.FineByStudentFragment;
import com.kumar.akshay.libmag.librarian.IssueABookFragment;

import java.io.IOException;

public class ScanBarcodeActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    TextView textView;
    int REQUEST_CODE, i = 0;
    String TAG = "ScanBarcodeActivity";
    String previousBarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        i = 0;
        textView = findViewById(R.id.scannerTextView);
        textView.setText(getIntent().getExtras().getString("textViewText"));
        REQUEST_CODE = getIntent().getExtras().getInt("req_code");
        cameraPreview = findViewById(R.id.camera_preview);
        createCameraSource();
    }

    private void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    if (REQUEST_CODE == 0) {
                        Log.v(TAG, "Request code 0 found");
                        Log.v(TAG, "Value of i = " + i);
                        switch (i) {
                            case 0:
                                previousBarcode = barcodes.valueAt(0).displayValue;
                                IssueABookFragment.bookId = barcodes.valueAt(0).displayValue;
                                Log.v(TAG, "First barcode scanned");
                                i++;
                                break;
                            case 1:
                                Log.v(TAG, "Previos barcode : " + previousBarcode);
                                Log.v(TAG, "Current barcode is " + barcodes.valueAt(0).displayValue);
                                if (!barcodes.valueAt(0).displayValue.equals(previousBarcode)) {
                                    IssueABookFragment.rollNo = barcodes.valueAt(0).displayValue;
                                    Log.v(TAG, "Second bar code scanned");
                                    finish();
                                }
                                break;
                            default:
                                Toast.makeText(ScanBarcodeActivity.this, "No barcode detected", Toast.LENGTH_SHORT).show();
                        }
                    } else if (REQUEST_CODE == 1) {
                        Log.v(TAG, "Request code 1 found");
                        IssueABookFragment.bookId = barcodes.valueAt(0).displayValue;
                        Log.v(TAG, "Barcode scanned");
                        finish();
                    } else if (REQUEST_CODE == 2) {
                        FineByStudentFragment.rollno = barcodes.valueAt(0).displayValue;
                        finish();
                    } else if (REQUEST_CODE == 3) {
                        FineByStudentFragment.rollno = barcodes.valueAt(0).displayValue;
                        finish();
                    } else if (REQUEST_CODE == 4) {
                        BookAvailability.id = barcodes.valueAt(0).displayValue;
                        finish();
                    }
//                    if (barcodes.size() > 0) {
//                    Intent intent = new Intent();
//                    intent.putExtra("barcode", barcodes.valueAt(0));
//                    setResult(CommonStatusCodes.SUCCESS, intent);
//                        finish();
//                    }
                }
            }
        });
    }
}
