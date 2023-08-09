package edu.northeastern.plantr;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;

import java.io.File;

public class cameraActivity extends AppCompatActivity {

    private Button takePhotoButton;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static{
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraID;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession myCaptureSession;
    protected CaptureRequest myCaptureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private ImageReader imageReader;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //textureView = (TextureView)findViewById(R.id.textureViewPhoto);
        //textureView.setSurfaceTextureListener(textureListener);
        //takePictureButton = (Button)findViewById(r.id.takePhotoButton);
    }

    protected void takePicture(){
        if(null == cameraDevice){
            return;
        }
        //CameraManager =
    }
}