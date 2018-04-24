package com.example.theproject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class CameraActivity extends Activity {

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
	public ImageView flipCamera;
	public ImageView exposureBtn;
	public String path;
	protected boolean isCircleDrawn = false;
	protected int[] exposureLevels;
	protected ArrayList<ImageThumbnail> thumbnailViews = new ArrayList<>();
	public boolean circleDrawn = false;
	public boolean klik = false;
	protected static int cameraRotation;

	public DrawCircle circle;

	ImageThumbnail _imageThumbnail;
	ScaleBitmap _scaleBitmap;
	public int currentCameraId;
	private ArrayList<Bitmap> bitmapList= new ArrayList<Bitmap>();

	private ArrayList<byte[]> byteList= new ArrayList<byte[]>();
	
	//TODO implement gallery
	//TODO save picture 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);

		if (getIntent().getStringExtra("path") == null) return;
		path = getIntent().getStringExtra("path");
		Toast.makeText(CameraActivity.this,path,Toast.LENGTH_SHORT).show();

		initCamera();
		initPreview();
		
		camParams = camera.getParameters(); //balans bieli 
		camParams.getExposureCompensation(); //kompensacja naswietlenia
		camParams.getSupportedPictureSizes(); // obslugiwane wielkosci robionego zdjecia np 640x480

		exposureLevels = new int[((camParams.getMaxExposureCompensation() * 2) + 1)];
		int j = 0;
		for(int i = camParams.getMinExposureCompensation(); i <= camParams.getMaxExposureCompensation(); i++) {
			exposureLevels[j++] = i;
		}

		whiteBalanceOptions = new String[(camParams.getSupportedWhiteBalance().size())];
		resolutionOptions = new Camera.Size[(camParams.getSupportedPictureSizes().size())];
		
		camParams.getSupportedPictureSizes().toArray(resolutionOptions);
		final String[] resolutionOptionsFinal = new String[resolutionOptions.length];
		
		for(int i = 0; i<resolutionOptions.length; i++){
			resolutionOptionsFinal[i] = resolutionOptions[i].width + "x" + resolutionOptions[i].height;
		}
		
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
		
		whiteBalance = (ImageView) findViewById(R.id.white_balance);
		whiteBalance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
				alert.setTitle("Balans bieli");
				
				camParams.getSupportedWhiteBalance().toArray(whiteBalanceOptions);
				alert.setItems(whiteBalanceOptions, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						camParams.setWhiteBalance(whiteBalanceOptions[which]);
						Toast.makeText(CameraActivity.this, "Balans bieli: " + whiteBalanceOptions[which], Toast.LENGTH_SHORT).show();
						camera.setParameters(camParams);
					}
				});
				alert.show();


				flipCamera = (ImageView) findViewById(R.id.flipCamera);

				flipCamera.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
					if(klik != false){
						Toast.makeText(CameraActivity.this,"Tylna", Toast.LENGTH_SHORT).show();

						klik=!klik;

					}else{
						Toast.makeText(CameraActivity.this,"Przednia", Toast.LENGTH_SHORT).show();


						klik = !klik;
					}
					}
				});
			}
		});

		final String[] expValues = new String[exposureLevels.length];
		for(int i = 0; i < exposureLevels.length; i++) {
			expValues[i] = exposureLevels[i] + "";
		}
		
		resolution = (ImageView) findViewById(R.id.resolution);
		resolution.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
				alert.setTitle("Rozdzielczość");
				alert.setItems(resolutionOptionsFinal, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						 camParams.setPictureSize(resolutionOptions[which].width, resolutionOptions[which].height);
                         camera.setParameters(camParams);
                         Toast.makeText(CameraActivity.this, "Zmieniono rozdzielczość ", Toast.LENGTH_SHORT).show();
                         
                        ;
					}
				});
				alert.show();
			}
			
			
		});
		exposureBtn = (ImageView) findViewById(R.id.exposure);

		exposureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder dialogExp = new AlertDialog.Builder(CameraActivity.this);
				dialogExp   .setTitle("Wartość ekspozycji")
						.setCancelable(true)
						.setItems(expValues, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								camParams.setExposureCompensation(exposureLevels[which]);
								Toast.makeText(CameraActivity.this, "Zmieniono wartość ekspozycji na " + expValues[which], Toast.LENGTH_SHORT).show();
								camera.setParameters(camParams);
							}
						})
						.show();
			}
		});


	}
	public void initPreview(){
		_cameraPreview = new CameraPreview(CameraActivity.this, camera);
		_frameLayout = (FrameLayout) findViewById(R.id.cameraLayout);
		_frameLayout.addView(_cameraPreview);

	}
	
	protected PictureCallback camPictureCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			final Rect displaySize = new Rect();

			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getRectSize(displaySize);



			if(!circleDrawn){
				circle = new DrawCircle(CameraActivity.this);
				_frameLayout.addView(circle);
				camera.startPreview();
				circleDrawn = !circleDrawn;
			} else{
				camera.startPreview();
			}

			int bitmapScaleFactor = (displaySize.width() > displaySize.height() ? displaySize.height() / 7 : displaySize.width() / 7);
			_scaleBitmap = new ScaleBitmap();
			//byteList.add(data);
			byteList.add(ImagingHelper.getRawRotatedFromRaw(data, cameraRotation));

			Bitmap scaledBitmap = ImagingHelper.makeScaledBitmapFromRaw(data, bitmapScaleFactor, bitmapScaleFactor);

			bitmapList.add(ScaleBitmap.bitmapConvert(data));


			ImageThumbnail _imageThumbnail  = new ImageThumbnail(CameraActivity.this, ImagingHelper.getCircularBitmap(scaledBitmap), circle.getPointOnCircle(0));

			_imageThumbnail.setId(0xaaaa0000 + thumbnailViews.size());


           _imageThumbnail.setOnLongClickListener(new View.OnLongClickListener() {
			   @Override
			   public boolean onLongClick(View v) {

				   final int selectedId = v.getId() - 0xaaaa0000;

				   final View view = v;

				   String[] options = {"Zobacz","Zapisz wybrane", "Zapisz wszystkie", "Usuń to zdjęcie", "Usuń wszystkie"};
				   AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
				   builder.setTitle("Zdjęcie: " + selectedId)
						   .setItems(options, new DialogInterface.OnClickListener() {
							   @Override
							   public void onClick(DialogInterface dialog, int which) {
								switch (which){
									case 0:
										AlertDialog.Builder photo = new AlertDialog.Builder(CameraActivity.this);
										photo.setTitle("PODGLĄD");
										ImageView image = new ImageView(CameraActivity.this);
										image.setImageBitmap(ImagingHelper.makeScaledBitmapFromRaw(byteList.get(selectedId), displaySize.width() / 2, displaySize.height() / 2));
										photo.setCancelable(true)
												.setView(image)
												.show();
										break;
									case 1:
										savePicture(byteList.get(selectedId));
										break;
									case 2:
										for (int i = 0; i < byteList.size(); i++) {
											savePicture(byteList.get(i));

										}
										break;
									case 3:
										byteList.remove(selectedId);
										thumbnailViews.remove(selectedId);
										_frameLayout.removeView(view);
										realignPictures();

										break;
									case 4:
										byteList.clear();
										for (ImageThumbnail thumbnail : thumbnailViews) {
											_frameLayout.removeView(thumbnail);
										}
										thumbnailViews.clear();
										break;
									default:
										return;

								}
							   }
						   })
						   .setCancelable(true)
						   .show();

				   return true;
			   }
		   });

//            _imageThumbnail.setOnTouchListener(new OnSwipeTouchListener(CameraActivity.this) {
//                public void onSwipeRight() {
//                    byteList.clear();
//                    for (ImageThumbnail thumbnail : thumbnailViews) {
//                        _frameLayout.removeView(thumbnail);
//                    }
//                    thumbnailViews.clear();
//                }
//
//            });

            _frameLayout.addView(_imageThumbnail);
			thumbnailViews.add(_imageThumbnail);

			realignPictures();
            
		}
	};
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if(level == TRIM_MEMORY_RUNNING_LOW) {
			System.gc();
		}
	}
	public void realignPictures() {
		double newPicAngle = (2 * Math.PI) / thumbnailViews.size();
		for (int i = 0; i < thumbnailViews.size(); i++) {
			thumbnailViews.get(i).setPosition(circle.getPointOnCircle(i * newPicAngle));
		}

	}

	public static void setCameraDisplayOrientation(Activity activity,
												   int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info =
				new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		cameraRotation = result;
		camera.setDisplayOrientation(result);
	}

	public void initCamera(){
	
	 boolean cam = getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);

        if (!cam) {
            // uwaga - brak kamery, przetestuj przy wyczonej
            // kamerze w emulatorze

        } else {

            // wykorzystanie danych zwrconych przez kolejna funkc getCameraId

            cameraId = getCameraId();
                // jest jaka kamera!
            if (cameraId < 0) {
                // brak kamery z przodu!
            } else if (cameraId >= 0) {
                camera = Camera.open(cameraId);
            } else {
                camera = Camera.open();
            }
			setCameraDisplayOrientation(this, cameraId, camera);
        }
	}

	public void savePicture(byte[] data) {

		SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
		String filename = dFormat.format(new Date());
		File newPhoto = new File(path + "/" + filename + ".jpg");
		try {
			FileOutputStream fos = new FileOutputStream(newPhoto);
			fos.write(data);
			fos.close();
		} catch (Exception e) {
			Toast.makeText(CameraActivity.this, "Nie można było zapisać zdjęcia", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
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
		                selectedCamera = i;// zwr (return) z funkcji i -> czyli aktywn kamer
		            }

//		            else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
//		            	selectedCamera = i;// zwr (return) z funkcji i -> czyli aktywn kame
//		          }

		}
		return selectedCamera;

	}


	

	@Override
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

}
