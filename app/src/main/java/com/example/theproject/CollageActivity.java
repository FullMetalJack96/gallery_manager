package com.example.theproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CollageActivity extends Activity {

	public ImageView collageImgView;
	public RelativeLayout collageLayout;
	private ArrayList<HashMap<String, Integer>> lista;
	//odczytanie w drugiej aktywnosci
	protected Point displaySize = new Point();
	protected int selectedId = 0;
	protected static int RESULT_LOAD_IMAGE = 1;
	private static final int CAMERA_REQUEST = 1888;
	protected Bitmap sourceBitmap;
	public int mirrorStatus = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collage);
		collageLayout = (RelativeLayout) findViewById(R.id.collageLayout);


		Bundle extras = getIntent().getExtras();

		ArrayList<ComponentLayout> components = (ArrayList<ComponentLayout>) extras.getSerializable("components");
		if (components == null) {
			Toast.makeText(CollageActivity.this, "Nie przekazano listy komponentów!", Toast.LENGTH_SHORT).show();
			finish();
		}


		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getSize(displaySize);

		try {
			for (int i = 0; i < components.size(); i++) {
				ComponentLayout component = components.get(i);
				final ImageView image = new ImageView(CollageActivity.this);
				image.setX(displaySize.x * component.getX());
				image.setY(displaySize.y * component.getY());
				image.setLayoutParams(new LinearLayout.LayoutParams((int) (displaySize.x * component.getWidth()), (int) (displaySize.y * component.getHeight())));
				image.setBackgroundColor(0xff2d2d2d);
				image.setPadding(5, 5, 0, 0);
				image.setBackgroundColor(0xff2d2d2d);
				image.setScaleType(ImageView.ScaleType.CENTER);
				image.setImageResource(R.drawable.collagecomponent);
				image.setId(10000 + i);
				image.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						selectedId = v.getId();

						AlertDialog.Builder builder = new AlertDialog.Builder(CollageActivity.this);
						String[] opcje = {"Galeria", "Własny aparat", "Wbudowany aparat"};

						builder.setTitle("Skąd pobrać zdjęcie?")
								.setItems(opcje, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										switch (which) {
											case 0:
												Toast.makeText(CollageActivity.this, "Galeria", Toast.LENGTH_SHORT).show();

												Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
												startActivityForResult(i, RESULT_LOAD_IMAGE);

												break;
											case 1:
												Intent startCamera = new Intent(CollageActivity.this, CollageCameraActivity.class);
												startActivityForResult(startCamera, 12345);
												break;
											case 2:

												Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
												startActivityForResult(cameraIntent, CAMERA_REQUEST);

												break;
											default:
												break;
										}
									}
								})
								.setNegativeButton("Anuluj", null)
								.show();


					}
				});
//				image.setOnLongClickListener(new View.OnLongClickListener() {
//					@Override
//					public boolean onLongClick(View v) {
//
//
//
//						final AlertDialog.Builder builder = new AlertDialog.Builder(CollageActivity.this);
//
//						final String[] opcje = {"Odbij lustrzanie element", "Obróć element o 90st", "Usuń element"};
//						try{
//							builder.setTitle("Co chcesz zrobić? ")
//									.setItems(opcje, new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(DialogInterface dialog, int which) {
//											switch (which) {
//												case 0:
//													mirrorStatus++;
//
//													if(mirrorStatus>3){
//														mirrorStatus = 0;
//													}
//													switch (mirrorStatus){
//														case 1:
//															mirror(-1.0f, 1.0f,image);
//															break;
//														case 2:
//															mirror(1.0f, -1.0f,image);
//															break;
//														case 3:
//															mirror(1.0f, -1.0f,image);
//															break;
//														case 4:
//															mirror(-1.0f, 1.0f,image);
//															break;
//													}
//													break;
//												case 1:
//													flipPicture(image);
//													break;
//												case 2:
//													collageLayout.removeView(image);
//													break;
//												default:
//													break;
//											}
//										}
//									})
//									.setNegativeButton("Anuluj", null)
//									.show();
//
//						}catch (Exception ex){
//							Toast.makeText(CollageActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
//						}
//						return false;
//					}
//
//				});
				collageLayout.addView(image);
			}
		} catch (Exception ex) {
			Toast.makeText(CollageActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
			finish();
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			ImageView selectedImageView = (ImageView) CollageActivity.this.findViewById(selectedId);
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			selectedImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		}else if (data != null && requestCode == 12345 ) {
			ImageView selectedImage = (ImageView) CollageActivity.this.findViewById(selectedId);
			if (data.getByteArrayExtra("data") != null) {
				Bitmap bitmap = ImagingHelper.getScaledBitmap(ImagingHelper.getRotatedBitmapFromRaw(data.getByteArrayExtra("data"), 90), selectedImage.getWidth(), selectedImage.getHeight());
				sourceBitmap = bitmap;
				selectedImage.setImageBitmap(bitmap);
			} else {

			}
		} else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			ImageView selectedImageView = (ImageView) CollageActivity.this.findViewById(selectedId);
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			selectedImageView.setImageBitmap(photo);
		}else if(requestCode == 66677){
			if(data!=null){
				ImageView selectedImageV = (ImageView) CollageActivity.this.findViewById(selectedId);
				Bitmap bitmap = ImagingHelper.getScaledBitmap(ImagingHelper.getRotatedBitmapFromRaw(data.getByteArrayExtra("data"), 90), selectedImageV.getWidth(), selectedImageV.getHeight());
				selectedImageV.setImageBitmap(bitmap);
				//Toast.makeText(CollageActivity.this, "Są dane;", Toast.LENGTH_SHORT).show();


			}else{
				Toast.makeText(CollageActivity.this, "nie ma danych", Toast.LENGTH_SHORT).show();
			}
		}
	}
	public void flipPicture(ImageView myImage) {
		Matrix transformMatrix = new Matrix();
		transformMatrix.postRotate(90);
		Bitmap source = ((BitmapDrawable) myImage.getDrawable()).getBitmap();
		Bitmap translated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), transformMatrix, false);
		Bitmap sourceCopy = sourceBitmap;
		sourceBitmap = Bitmap.createBitmap(sourceCopy, 0, 0, sourceCopy.getWidth(), sourceCopy.getHeight(), transformMatrix, false);
		myImage.setImageBitmap(translated);
	}

	public void mirror(float x, float y, ImageView myImage) {
		Matrix transformMatrix = new Matrix();
		transformMatrix.postScale(x, y);
		Bitmap source = ((BitmapDrawable) myImage.getDrawable()).getBitmap();
		Bitmap translated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), transformMatrix, false);
		Bitmap sourceCopy = sourceBitmap;
		sourceBitmap = Bitmap.createBitmap(sourceCopy, 0, 0, sourceCopy.getWidth(), sourceCopy.getHeight(), transformMatrix, false);
		myImage.setImageBitmap(translated);
	}

}
