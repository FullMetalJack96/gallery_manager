package com.example.theproject;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class CollageCameraActivity extends Activity {

	Camera camera;
	int cameraId = -1;
	CameraPreview _cameraPreview;
	FrameLayout _frameLayout;
	private Camera.Parameters camParams;
	
	public ImageView whiteBalance;
	public ImageView resolution;
	public String[] whiteBalanceOptions;
	public Camera.Size[] resolutionOptions;
	public FrameLayout cameraLayout;
	public ImageView shutter;
	protected ImageView btnAccept, btnReject;

	public boolean circleDrawn = false;
	DrawCircle _drawCircle;
	ImageThumbnail _imageThumbnail;
	ScaleBitmap _scaleBitmap;
	private ArrayList<Bitmap> bitmapList= new ArrayList<Bitmap>();
	private ArrayList<byte[]> byteList= new ArrayList<byte[]>();
	
	//TODO implement gallery
	//TODO save picture 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collage_camera);
		btnAccept = (ImageView) findViewById(R.id.accept);
		btnReject = (ImageView) findViewById(R.id.cancel);
		initCamera();
		initPreview();
		
		
	
		
		shutter = (ImageView) findViewById(R.id.shutter);
		shutter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				camera.takePicture(null, null, camPictureCallback);
				
			}
		});
		cameraLayout = (FrameLayout) findViewById(R.id.cameraLayout);
		cameraLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				camera.autoFocus(null);
			}
		});
		
		
	}
	
	protected PictureCallback camPictureCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
           final byte[] fdata = data;

			btnAccept.setVisibility(View.VISIBLE);
			btnReject.setVisibility(View.VISIBLE);

			btnReject.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					byte[] data = new byte[0];
					Intent returnIntent = new Intent();
					returnIntent.putExtra("data", data);
					setResult(12345, returnIntent);
					CollageCameraActivity.this.finish();
				}
			});

			btnAccept.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent returnIntent = new Intent();
					Bitmap data = BitmapFactory.decodeByteArray(fdata, 0, fdata.length);
					Bitmap dataSmall = ImagingHelper.getScaledBitmap(data, data.getWidth() / 4, data.getHeight() / 4);
					byte[] returned = ImagingHelper.getRawFromBitmap(dataSmall);
					returnIntent.putExtra("data", returned);
					setResult(12345, returnIntent);
					//Toast.makeText(CollageCameraActivity.this, "Wyjście i zapisanie danych", Toast.LENGTH_SHORT).show();
					CollageCameraActivity.this.finish();
				}
			});
           
            	camera.startPreview();
		}
	}; 

	public void initCamera(){
	
	 boolean cam = getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);

        if (!cam) {
            // uwaga - brak kamery, przetestuj przy waczonej
            // kamerze w emulatorze

        } else {

            // wykorzystanie danych zwrconych przez kolejna funkcj getCameraId

            cameraId = getCameraId();
                // jest jaka kamera!
            if (cameraId < 0) {
                // brak kamery z przodu!
            } else if (cameraId >= 0) {
                camera = Camera.open(cameraId);
            } else {
                camera = Camera.open();
            }

        }
	}
	
	public int getCameraId(){
		int camerasCount = Camera.getNumberOfCameras(); // pobranie referencji do kamer
        int selectedCamera = 0;
		
		for (int i = 0; i < camerasCount; i++) {
		    CameraInfo cameraInfo = new CameraInfo();
		    Camera.getCameraInfo(i, cameraInfo);
		            //
		            // 0 - back camera
		            // 1 - front camera

		            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
		                selectedCamera = i;// zwr (return) z funkcji i -> czyli akty
		            }

		           // else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
		          //  	selectedCamera = i;// zw (return) z funkcji i -> czyli akty
		          //  }

		}
		return selectedCamera;
		
	}
	
	public void initPreview(){
		_cameraPreview = new CameraPreview(CollageCameraActivity.this, camera);
		_frameLayout = (FrameLayout) findViewById(R.id.cameraLayout);
		_frameLayout.addView(_cameraPreview);
		
	}
	


}
